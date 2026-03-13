package seedu.duke.command;

public class SendCommand extends Command {
    private static final String HELP_DESCRIPTION = "format: help [COMMAND]\n" +
            "COMMAND is optional\n" +
            "If no COMMAND is given: lists all the available commands\n" +
            "If a COMMAND is given: displays details regarding that command";

    public SendCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description) {
        System.out.println("send command executed");
    }
}
