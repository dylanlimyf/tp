package seedu.duke.command;

public enum CommandWord {
    HELP, EXIT,

    // Wallet related commands
    CREATE, LIST, KEYGEN,
    BALANCE, SEND,

    // Blockchain related commands
    VALIDATE, VIEWBLOCK
}
