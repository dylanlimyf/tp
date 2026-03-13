package seedu.duke.command;

public class CreateCommand extends Command {
    private static final String HELP_DESCRIPTION = "format: create NAME\n" +
            "COMMAND is optional\n" +
            "If no COMMAND is given: lists all the available commands\n" +
            "If a COMMAND is given: displays details regarding that command";

    public CreateCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description) {
        System.out.println("create command executed");
    }
}
