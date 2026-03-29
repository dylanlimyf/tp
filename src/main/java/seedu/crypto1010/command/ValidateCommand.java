package seedu.crypto1010.command;

import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;
import seedu.crypto1010.model.ValidationResult;

public class ValidateCommand extends Command {
    private static final String HELP_DESCRIPTION = """
            Format: validate
            
            Validates entire blockchain integrity
            """;
    private static final String INVALID_FORMAT_ERROR = "Error: Invalid validate format. Use: validate";

    public ValidateCommand() {
        super(HELP_DESCRIPTION);
    }

    @Override
    public void execute(String description, Blockchain blockchain) throws Crypto1010Exception {
        if (description != null && !description.isBlank()) {
            throw new Crypto1010Exception(INVALID_FORMAT_ERROR);
        }

        ValidationResult result = blockchain.validate();
        if (result.isValid()) {
            System.out.println("Blockchain is valid. All blocks verified successfully.");
        } else {
            System.out.println("Blockchain is invalid. Reason: " + result.getReason());
        }
    }
}
