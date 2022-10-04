package de.fr3qu3ncy.easytools.core.sql;

import java.sql.*;

public final class SQLCore {

    private SQLCore() {}

    public static void createTable(Connection connection, SQLTable table) {
        update(connection, "CREATE TABLE IF NOT EXISTS " + table.format());
    }

    public static void update(Connection connection, String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static <T> T queryObject(Connection connection, String sql, Class<T> className, String columnName) {
        try (PreparedStatement statement = connection.prepareStatement(sql) ; ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getObject(columnName, className);
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
