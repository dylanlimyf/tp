package seedu.duke.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WalletManager {
    private final List<Wallet> wallets;

    public WalletManager() {
        this.wallets = new ArrayList<>();
    }

    public Wallet createWallet(String walletName) {
        Wallet wallet = new Wallet(walletName);
        wallets.add(wallet);
        return wallet;
    }

    public boolean hasWallet(String walletName) {
        String normalizedName = walletName.trim();
        return wallets.stream()
                .anyMatch(wallet -> wallet.getName().equalsIgnoreCase(normalizedName));
    }

    public List<Wallet> getWallets() {
        return Collections.unmodifiableList(wallets);
    }
}
