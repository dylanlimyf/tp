package seedu.crypto1010.command;

import seedu.crypto1010.Crypto1010;
import seedu.crypto1010.Parser;
import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;
import seedu.crypto1010.model.WalletManager;
import seedu.crypto1010.storage.WalletStorage;

import java.util.Scanner;

public class TutorialCommand extends Command {
    private static final String HELP_DESCRIPTION = """
            Format: help [c/COMMAND]
            Example: help c/list
            
            COMMAND is optional
            If no valid COMMAND is given: lists all the available commands
            If a valid COMMAND is given: displays details regarding that command
            """;

    private static final String ERROR_MESSAGE = "Please input the given command to continue\n" +
            "If you want to exit tutorial mode, type: tutorial exit";
    public TutorialCommand() {
        super(HELP_DESCRIPTION);
    }

    public void execute(String description, Blockchain blockchain) {
        Scanner in = new Scanner(System.in);
        WalletStorage walletStorage = new WalletStorage(Crypto1010.class);
        WalletManager walletManager = new WalletManager();
        Parser parser = new Parser(walletManager);

        String[] instructions = {
                "create w/alice",
                "create w/bob",
                "keygen w/alice",
                "keygen w/bob",
                "list",
                "balance w/alice",
                "balance w/bob",
                "help c/send",
                "send w/bob amt/3 to/",
                "balance w/alice",
                "balance w/bob",
        };
        int index = 0;

        while (true) {
            System.out.println("Enter the following command:");
            System.out.println(instructions[index]);
            String input = in.nextLine().strip();
            if (input.equals("tutorial exit")) {

            } else if (input.equals(instructions[index])) {
                // Do the executing
                Command c = parser.parse(input);
                String[] components = input.split("\\s+", 2);
                String descriptions = components.length > 1 ? components[1] : "";
                try {
                    c.execute(descriptions, blockchain);
                    index++;
                } catch (Crypto1010Exception e) {
                    System.out.println(ERROR_MESSAGE);
                }
            } else if (input.equals("exit")) {
                return;
            } else {
                System.out.println("That was not the given instruction");
                System.out.println("If you wish to exit type: exit()");
            }
        }
    }
}
