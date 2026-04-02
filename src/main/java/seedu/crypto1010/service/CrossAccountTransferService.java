package seedu.crypto1010.service;

import seedu.crypto1010.auth.AccountCredentials;
import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;
import seedu.crypto1010.model.CurrencyCode;
import seedu.crypto1010.model.Wallet;
import seedu.crypto1010.model.WalletManager;
import seedu.crypto1010.storage.AccountStorage;
import seedu.crypto1010.storage.BlockchainStorage;
import seedu.crypto1010.storage.WalletStorage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CrossAccountTransferService {
    static final String RECIPIENT_NOT_FOUND_ERROR = "Error: Recipient account not found.";
    static final String SAME_ACCOUNT_ERROR = "Error: Cannot send to your own account.";
    static final String SENDER_WALLET_NOT_FOUND_ERROR = "Error: No wallet found for currency '%s'.";
    static final String DUPLICATE_CURRENCY_WALLET_ERROR =
            "Error: Multiple wallets found for currency '%s'. Use exactly one wallet per currency.";
    static final String ACCOUNT_DATA_LOAD_ERROR = "Error: Failed to load account data.";
    static final String RECIPIENT_DATA_LOAD_ERROR = "Error: Failed to load recipient account data.";
    static final String RECIPIENT_DATA_SAVE_ERROR = "Error: Failed to save recipient account data.";

    private static final String EXTERNAL_ACCOUNT_PREFIX = "external:";

    private final WalletManager currentWalletManager;
    private final String currentAccountName;
    private final Class<?> storageAnchor;

    public CrossAccountTransferService(WalletManager currentWalletManager, String currentAccountName,
                                       Class<?> storageAnchor) {
        this.currentWalletManager = Objects.requireNonNull(currentWalletManager);
        this.currentAccountName = normalizeAccountName(currentAccountName);
        this.storageAnchor = Objects.requireNonNull(storageAnchor);
    }

    public CrossAccountTransferResult transfer(String recipientAccountName, BigDecimal amount, String currencyCode,
                                               Blockchain senderBlockchain) throws Crypto1010Exception {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(senderBlockchain);

        if (currentAccountName.isBlank()) {
            throw new Crypto1010Exception(ACCOUNT_DATA_LOAD_ERROR);
        }

        String normalizedRecipientAccountName = normalizeAccountName(recipientAccountName);
        String normalizedCurrencyCode = CurrencyCode.normalize(currencyCode);

        if (normalizedRecipientAccountName.equals(currentAccountName)) {
            throw new Crypto1010Exception(SAME_ACCOUNT_ERROR);
        }
        if (!accountExists(normalizedRecipientAccountName)) {
            throw new Crypto1010Exception(RECIPIENT_NOT_FOUND_ERROR);
        }

        Wallet senderWallet = resolveSingleCurrencyWallet(currentWalletManager, normalizedCurrencyCode);
        BigDecimal senderBalance = senderBlockchain.getPreciseBalance(senderWallet.getName());
        if (senderBalance.compareTo(amount) < 0) {
            throw new Crypto1010Exception(TransactionRecordingService.INSUFFICIENT_BALANCE_ERROR);
        }

        WalletStorage recipientWalletStorage = new WalletStorage(storageAnchor, normalizedRecipientAccountName);
        BlockchainStorage recipientBlockchainStorage =
                new BlockchainStorage(storageAnchor, normalizedRecipientAccountName);

        WalletManager recipientWalletManager = loadRecipientWalletManager(recipientWalletStorage);
        Blockchain recipientBlockchain = loadRecipientBlockchain(recipientBlockchainStorage);

        RecipientWalletResolution recipientResolution =
                resolveOrCreateRecipientWallet(recipientWalletManager, normalizedCurrencyCode);

        saveRecipientWalletManager(recipientWalletStorage, recipientWalletManager);
        recipientBlockchain.addTransactions(List.of(formatRecipientTransaction(
                recipientResolution.wallet().getName(),
                normalizedCurrencyCode,
                amount)));
        saveRecipientBlockchain(recipientBlockchainStorage, recipientBlockchain);

        senderBlockchain.addTransactions(List.of(formatSenderTransaction(
                senderWallet.getName(),
                normalizedRecipientAccountName,
                normalizedCurrencyCode,
                amount)));
        senderWallet.addTransaction(buildHistoryEntry(normalizedRecipientAccountName, amount, normalizedCurrencyCode));

        return new CrossAccountTransferResult(senderWallet.getName(), recipientResolution.wallet().getName(),
                recipientResolution.wasCreated());
    }

    private boolean accountExists(String recipientAccountName) throws Crypto1010Exception {
        AccountStorage accountStorage = new AccountStorage(storageAnchor);
        try {
            List<AccountCredentials> accountCredentials = accountStorage.load();
            return accountCredentials.stream()
                    .map(AccountCredentials::username)
                    .anyMatch(username -> username.equalsIgnoreCase(recipientAccountName));
        } catch (IOException e) {
            throw new Crypto1010Exception(ACCOUNT_DATA_LOAD_ERROR);
        }
    }

    private Wallet resolveSingleCurrencyWallet(WalletManager walletManager, String currencyCode)
            throws Crypto1010Exception {
        List<Wallet> matchingWallets = walletManager.findWalletsByCurrency(currencyCode);
        if (matchingWallets.isEmpty()) {
            throw new Crypto1010Exception(String.format(Locale.ROOT, SENDER_WALLET_NOT_FOUND_ERROR, currencyCode));
        }
        if (matchingWallets.size() > 1) {
            throw new Crypto1010Exception(String.format(Locale.ROOT, DUPLICATE_CURRENCY_WALLET_ERROR, currencyCode));
        }
        return matchingWallets.get(0);
    }

    private WalletManager loadRecipientWalletManager(WalletStorage recipientWalletStorage) throws Crypto1010Exception {
        try {
            return recipientWalletStorage.load();
        } catch (IOException e) {
            throw new Crypto1010Exception(RECIPIENT_DATA_LOAD_ERROR);
        }
    }

    private Blockchain loadRecipientBlockchain(BlockchainStorage recipientBlockchainStorage)
            throws Crypto1010Exception {
        try {
            return recipientBlockchainStorage.load();
        } catch (IOException e) {
            throw new Crypto1010Exception(RECIPIENT_DATA_LOAD_ERROR);
        }
    }

    private RecipientWalletResolution resolveOrCreateRecipientWallet(WalletManager recipientWalletManager,
                                                                     String currencyCode)
            throws Crypto1010Exception {
        List<Wallet> matchingWallets = recipientWalletManager.findWalletsByCurrency(currencyCode);
        if (matchingWallets.size() > 1) {
            throw new Crypto1010Exception(String.format(Locale.ROOT, DUPLICATE_CURRENCY_WALLET_ERROR, currencyCode));
        }
        if (!matchingWallets.isEmpty()) {
            return new RecipientWalletResolution(matchingWallets.get(0), false);
        }

        String walletName = generateRecipientWalletName(recipientWalletManager, currencyCode);
        Wallet createdWallet = recipientWalletManager.createWallet(walletName, currencyCode);
        return new RecipientWalletResolution(createdWallet, true);
    }

    private String generateRecipientWalletName(WalletManager walletManager, String currencyCode) {
        if (!walletManager.hasWallet(currencyCode)) {
            return currencyCode;
        }

        String baseName = currencyCode + "-wallet";
        if (!walletManager.hasWallet(baseName)) {
            return baseName;
        }

        int suffix = 2;
        while (walletManager.hasWallet(baseName + "-" + suffix)) {
            suffix++;
        }
        return baseName + "-" + suffix;
    }

    private String formatSenderTransaction(String senderWalletName, String recipientAccountName,
                                           String currencyCode, BigDecimal amount) {
        return senderWalletName + " -> " + EXTERNAL_ACCOUNT_PREFIX + recipientAccountName + ":" + currencyCode
                + " : " + amount.stripTrailingZeros().toPlainString();
    }

    private String formatRecipientTransaction(String recipientWalletName, String currencyCode, BigDecimal amount) {
        return EXTERNAL_ACCOUNT_PREFIX + currentAccountName + ":" + currencyCode + " -> " + recipientWalletName
                + " : " + amount.stripTrailingZeros().toPlainString();
    }

    private void saveRecipientWalletManager(WalletStorage recipientWalletStorage, WalletManager recipientWalletManager)
            throws Crypto1010Exception {
        try {
            recipientWalletStorage.save(recipientWalletManager);
        } catch (IOException e) {
            throw new Crypto1010Exception(RECIPIENT_DATA_SAVE_ERROR);
        }
    }

    private void saveRecipientBlockchain(BlockchainStorage recipientBlockchainStorage, Blockchain recipientBlockchain)
            throws Crypto1010Exception {
        try {
            recipientBlockchainStorage.save(recipientBlockchain);
        } catch (IOException e) {
            throw new Crypto1010Exception(RECIPIENT_DATA_SAVE_ERROR);
        }
    }

    private String buildHistoryEntry(String recipientAccountName, BigDecimal amount, String currencyCode) {
        return "crossSend acc/" + recipientAccountName
                + " amt/" + amount.stripTrailingZeros().toPlainString()
                + " curr/" + currencyCode;
    }

    private String normalizeAccountName(String accountName) {
        return accountName == null ? "" : accountName.trim().toLowerCase(Locale.ROOT);
    }

    private record RecipientWalletResolution(Wallet wallet, boolean wasCreated) {
    }

    public record CrossAccountTransferResult(String senderWalletName, String recipientWalletName,
                                             boolean recipientWalletCreated) {
    }
}
