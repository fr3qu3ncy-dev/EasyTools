package de.fr3qu3ncy.easytools.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SQLDataType {

    private final String type;

    public static final SQLDataType TEXT = create("TEXT");
    public static final SQLDataType INT = create("INT");
    public static final SQLDataType DOUBLE = create("DOUBLE");
    public static final SQLDataType BOOL = create("BOOL");

    public static SQLDataType varchar(int length) {
        return create("VARCHAR (" + length + ")");
    }

    public static SQLDataType fixedChar(int length) {
        return create("CHAR (" + length + ")");
    }

    public String format() {
        return type;
    }

    public static SQLDataType create(String type) {
        return new SQLDataType(type);
    }
}
