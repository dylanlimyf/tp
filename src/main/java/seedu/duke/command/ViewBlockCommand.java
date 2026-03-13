package seedu.duke.command;

public class ViewBlockCommand extends Command {
    private static final String HELP_DESCRIPTION = "format: viewblock INDEX\n" +
            "Displays the full details of a block\n" +
            "Details include: Block Index, Timestamp, Previous Hash, Current Hash and List of Transactions\n";

    public ViewBlockCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description) {
        System.out.println("view block command executed");
    }
}
