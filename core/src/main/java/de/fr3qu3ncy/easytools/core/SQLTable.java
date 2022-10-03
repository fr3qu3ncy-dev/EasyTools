package de.fr3qu3ncy.easytools.core;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public final class SQLTable {

    private final String name;
    private final List<SQLColumn> columns = new LinkedList<>();

    public SQLTable addColumn(String columnName, SQLDataType dataType, SQLConstraint... constraints) {
        columns.add(new SQLColumn(columnName, dataType, constraints));
        return this;
    }

    public String format() {
        StringBuilder formatted = new StringBuilder(name + " (");
        columns.forEach(dataType -> formatted.append(dataType.format()));
        return formatted.toString();
    }
}
