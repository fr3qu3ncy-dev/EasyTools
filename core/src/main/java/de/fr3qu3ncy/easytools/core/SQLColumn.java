package de.fr3qu3ncy.easytools.core;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class SQLColumn {

    private final String name;
    private final SQLDataType dataType;
    private final SQLConstraint[] constraints;

    public SQLColumn(String name, SQLDataType dataType, SQLConstraint... constraints) {
        this.name = name;
        this.dataType = dataType;
        this.constraints = constraints;
    }

    public String format() {
        StringBuilder sb = new StringBuilder(name + " " + dataType.format());
        Arrays.stream(constraints).forEach(c -> sb.append(" ").append(c.format()));
        return sb.toString();
    }
}
