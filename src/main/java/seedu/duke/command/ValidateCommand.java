package seedu.duke.command;

public class ValidateCommand extends Command {
    private static final String HELP_DESCRIPTION = "format: validate\n" +
            "Validates entire blockchain integrity";

    public ValidateCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description) {
        System.out.println("validate command executed");
    }
}
