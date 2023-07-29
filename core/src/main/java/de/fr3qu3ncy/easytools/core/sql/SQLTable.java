package de.fr3qu3ncy.easytools.core.sql;

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
        for (int i = 0 ; i < columns.size() ; i++) {
            SQLColumn column = columns.get(i);
            formatted.append(column.format());

            if (i + 1 < columns.size()) {
                formatted.append(",");
            }
        }
        formatted.append(")");
        return formatted.toString();
    }
}
