package seedu.duke;

import seedu.duke.command.Command;

import java.util.Scanner;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Parser parser = new Parser();

        while (true) {
            String message = in.nextLine().strip();
            String[] components = message.split("\\s+", 2);
            try {
                Command c = parser.parse(components[0]);
                c.execute();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Command");
            }
        }
    }
}
