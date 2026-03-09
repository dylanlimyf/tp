package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class KeygenCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("keygen command executed");
    }
}
