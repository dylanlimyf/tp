package seedu.duke.command;

public class ExitCommand extends Command {
    @Override
    public void execute() {
        System.out.println("exit command executed");
    }
}
