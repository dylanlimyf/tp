package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class CreateCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("create command executed");
    }
}
