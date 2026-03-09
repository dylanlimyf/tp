package seedu.duke.command;

import seedu.duke.model.Blockchain;

public class SendCommand extends Command {
    @Override
    public void execute(Blockchain blockchain) {
        System.out.println("send command executed");
    }
}
