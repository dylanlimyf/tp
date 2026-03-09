package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class HelpCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("help command executed");
    }
}
