package de.fr3qu3ncy.easytools.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLConstraint {

    private final String name;

    public static final SQLConstraint NOT_NULL = new SQLConstraint("NOT NULL");
    public static final SQLConstraint PRIMARY_KEY = new SQLConstraint("PRIMARY_KEY");

    public String format() {
        return name;
    }
}
