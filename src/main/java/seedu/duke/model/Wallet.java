package seedu.duke.model;

import java.util.Objects;

public class Wallet {
    private final String name;

    public Wallet(String name) {
        this.name = Objects.requireNonNull(name).trim();
    }

    public String getName() {
        return name;
    }
}
