package seedu.crypto1010;

import seedu.crypto1010.command.Command;
import seedu.crypto1010.command.ExitCommand;
import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;
import seedu.crypto1010.model.WalletManager;
import seedu.crypto1010.storage.BlockchainStorage;
import seedu.crypto1010.storage.WalletStorage;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Crypto1010 {
    private static final String DIVIDER =
            "============================================================";
    private static final String WELCOME_BANNER = """
            Welcome to Crypto1010
            Manage wallets, send transactions, and inspect your blockchain quickly.
            Try: create w/MainWallet | list | help
            """;

    /**
     * Main entry-point for the java.crypto1010.Crypto1010 application.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        printWelcome();
        BlockchainStorage blockchainStorage = new BlockchainStorage(Crypto1010.class);
        WalletStorage walletStorage = new WalletStorage(Crypto1010.class);
        LoadResult<Blockchain> blockchainLoadResult = loadBlockchain(blockchainStorage);
        LoadResult<WalletManager> walletLoadResult = loadWalletManager(walletStorage);
        Blockchain blockchain = blockchainLoadResult.data();
        WalletManager walletManager = walletLoadResult.data();
        boolean allowBlockchainSave = blockchainLoadResult.loadedSuccessfully();
        boolean allowWalletSave = walletLoadResult.loadedSuccessfully();
        if (!allowBlockchainSave) {
            System.out.println("Blockchain save is disabled to avoid overwriting existing data after load failure.");
        }
        if (!allowWalletSave) {
            System.out.println("Wallet save is disabled to avoid overwriting existing data after load failure.");
        }
        Parser parser = new Parser(walletManager);

        while (true) {
            String message;
            try {
                message = in.nextLine().strip();
            } catch (NoSuchElementException e) {
                saveData(
                        blockchainStorage,
                        walletStorage,
                        blockchain,
                        walletManager,
                        allowBlockchainSave,
                        allowWalletSave);
                break;
            }
            try {
                String[] components = message.split("\\s+", 2);
                String description = components.length > 1 ? components[1] : "";
                Command c;
                try {
                    c = parser.parse(message);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid command. Use: help c/COMMAND");
                    continue;
                }
                if (c instanceof ExitCommand) {
                    c.execute(description, blockchain);
                    saveData(
                            blockchainStorage,
                            walletStorage,
                            blockchain,
                            walletManager,
                            allowBlockchainSave,
                            allowWalletSave);
                    break;
                }
                c.execute(description, blockchain);
                saveData(
                        blockchainStorage,
                        walletStorage,
                        blockchain,
                        walletManager,
                        allowBlockchainSave,
                        allowWalletSave);
            } catch (Crypto1010Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void printWelcome() {
        System.out.println(DIVIDER);
        System.out.print(WELCOME_BANNER);
        System.out.println(DIVIDER);
    }

    private static LoadResult<Blockchain> loadBlockchain(BlockchainStorage storage) {
        try {
            return new LoadResult<>(storage.load(), true);
        } catch (IOException e) {
            System.out.println("Failed to load blockchain data. Starting with default blockchain.");
            return new LoadResult<>(Blockchain.createDefault(), false);
        }
    }

    private static LoadResult<WalletManager> loadWalletManager(WalletStorage storage) {
        try {
            return new LoadResult<>(storage.load(), true);
        } catch (IOException e) {
            System.out.println("Failed to load wallet data. Starting with empty wallet list.");
            return new LoadResult<>(new WalletManager(), false);
        }
    }

    private static void saveData(
            BlockchainStorage blockchainStorage,
            WalletStorage walletStorage,
            Blockchain blockchain,
            WalletManager walletManager,
            boolean allowBlockchainSave,
            boolean allowWalletSave) {
        if (allowBlockchainSave) {
            try {
                blockchainStorage.save(blockchain);
            } catch (IOException e) {
                System.out.println("Failed to save blockchain data.");
            }
        }
        if (allowWalletSave) {
            try {
                walletStorage.save(walletManager);
            } catch (IOException e) {
                System.out.println("Failed to save wallet data.");
            }
        }
    }

    private record LoadResult<T>(T data, boolean loadedSuccessfully) {
    }
}
