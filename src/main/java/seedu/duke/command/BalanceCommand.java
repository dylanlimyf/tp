package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class BalanceCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("balance command executed");
    }
}
