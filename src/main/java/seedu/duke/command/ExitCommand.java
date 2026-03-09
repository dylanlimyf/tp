package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class ExitCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("exit command executed");
    }
}
