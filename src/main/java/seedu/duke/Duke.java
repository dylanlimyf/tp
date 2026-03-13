package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.command.ExitCommand;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String message = in.nextLine().strip();
            try {
                String[] components = message.split("\\s+", 2);
                Command c = Parser.parse(components[0]);
                if (c instanceof ExitCommand) {
                    break;
                }
                c.execute(components[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Command");
            } catch (NoSuchElementException e) {
                System.out.println("No Input");
            } catch (ArrayIndexOutOfBoundsException e) {
                String[] components = message.split("\\s+", 2);
                Command c = Parser.parse(components[0]);
                c.execute("");
            }
        }
    }
}
