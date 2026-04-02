package seedu.crypto1010.command;

import org.junit.jupiter.api.Test;
import seedu.crypto1010.exceptions.Crypto1010Exception;
import seedu.crypto1010.model.Blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TutorialCommandTest {

    @Test
    public void execute_invalidFormat_throwsException() {
        TutorialCommand command = new TutorialCommand();
        Blockchain blockchain = Blockchain.createDefault();

        Crypto1010Exception thrown = assertThrows(
                Crypto1010Exception.class,
                () -> command.execute("wrong", blockchain)
        );

        assertEquals("Error: Invalid tutorial format. Use tutorial start", thrown.getMessage());
    }

    @Test
    public void constructor_createsInstance_success() {
        TutorialCommand command = new TutorialCommand();
        assertNotNull(command);
    }
}
