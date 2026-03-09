package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.command.ExitCommand;
import seedu.duke.model.Blockchain;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Parser parser = new Parser();
        Blockchain blockchain = Blockchain.createDefault();

        while (true) {
            try {
                String message = in.nextLine().strip();
                Command c = parser.parse(message);
                if (c instanceof ExitCommand) {
                    break;
                }
                c.execute(blockchain);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Command");
            } catch (NoSuchElementException e) {
                System.out.println("No Input");
            }
        }
    }
}
