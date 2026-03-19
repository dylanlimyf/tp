package seedu.duke.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.exceptions.Exceptions;
import seedu.duke.model.Blockchain;
import seedu.duke.model.WalletManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class KeygenCommandTest {
    @Test
    void execute_caseInsensitiveWalletName_printsSuccess() throws Exceptions {
        Blockchain blockchain = Blockchain.createDefault();
        WalletManager walletManager = new WalletManager();
        walletManager.createWallet("bob");
        KeygenCommand command = new KeygenCommand("w/BOB", walletManager);

        String output = runCommand(command, blockchain);

        assertTrue(output.contains("Key pair successfully generated"));
    }

    private String runCommand(Command command, Blockchain blockchain) throws Exceptions {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        try {
            command.execute(blockchain);
        } finally {
            System.setOut(originalOut);
        }
        return outputStream.toString();
    }
}
