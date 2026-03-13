package seedu.duke.command;

public class BalanceCommand extends Command {
    private static final String HELP_DESCRIPTION = "Format: balance NAME\n" +
            "Displays the balance of wallet up to 8 decimal points";

    public BalanceCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description) {
        System.out.println("balance command executed");
    }
}
