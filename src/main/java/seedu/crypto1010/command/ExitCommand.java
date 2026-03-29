package seedu.crypto1010.command;

import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;

public class ExitCommand extends Command {
    private static final String HELP_DESCRIPTION = """
            Format: exit
            Exits the program
            """;
    private static final String INVALID_FORMAT_ERROR = "Error: Invalid exit format. Use: exit";

    public ExitCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description, Blockchain blockchain) throws Crypto1010Exception {
        if (description != null && !description.isBlank()) {
            throw new Crypto1010Exception(INVALID_FORMAT_ERROR);
        }
    }
}
