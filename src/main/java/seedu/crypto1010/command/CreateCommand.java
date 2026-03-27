package seedu.crypto1010.command;

import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;
import seedu.crypto1010.model.Wallet;
import seedu.crypto1010.model.WalletManager;

import java.util.Objects;

public class CreateCommand extends Command {
    private static final String HELP_DESCRIPTION = """
            Format: create w/WALLET_NAME
            Example: create w/BobWallet
            
            Creates a wallet with the associated NAME
            NAME must be one word without spaces
            """;
  
    private static final String NAME_ERROR = "Error: wallet name cannot be empty.";
    private static final String NAME_WHITESPACE_ERROR = "Error: wallet name must be one word without spaces.";
    private static final String DUPLICATE_ERROR = "Error: wallet name already exists.";
    private static final String INVALID_FORMAT_ERROR = "Error: Invalid create format. Use: create w/WALLET_NAME";
    private static final String CREATE_FORMAT = "Use: create w/WALLET_NAME";

    private final String arguments;
    private final WalletManager walletManager;

    public CreateCommand(String arguments, WalletManager walletManager) {
        super(HELP_DESCRIPTION);
        this.arguments = arguments;
        this.walletManager = Objects.requireNonNull(walletManager);
    }

    @Override
    public void execute(String description, Blockchain blockchain) throws Crypto1010Exception {
        String walletName = parseArguments(resolveArguments(description));

        if (walletManager.hasWallet(walletName)) {
            throw new Crypto1010Exception(DUPLICATE_ERROR);
        }

        Wallet wallet = walletManager.createWallet(walletName);
        System.out.println("Wallet created: " + wallet.getName());
    }

    private String resolveArguments(String description) {
        if (arguments == null || arguments.isBlank()) {
            return description;
        }
        return arguments;
    }

    private String parseArguments(String args) throws Crypto1010Exception {
        if (args == null || args.isBlank()) {
            throw new Crypto1010Exception(NAME_ERROR + " " + CREATE_FORMAT);
        }

        String trimmedArgs = args.trim();
        if (!trimmedArgs.startsWith("w/")) {
            throw new Crypto1010Exception(INVALID_FORMAT_ERROR);
        }

        String walletName = trimmedArgs.substring(2).trim();
        if (walletName.isEmpty()) {
            throw new Crypto1010Exception(NAME_ERROR + " " + CREATE_FORMAT);
        }
        if (walletName.chars().anyMatch(Character::isWhitespace)) {
            throw new Crypto1010Exception(NAME_WHITESPACE_ERROR + " " + CREATE_FORMAT);
        }

        return walletName;
    }
}
