package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class ListCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("list command executed");
    }
}
