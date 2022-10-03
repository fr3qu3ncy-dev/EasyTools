package de.fr3qu3ncy.easytools.core;

import lombok.RequiredArgsConstructor;

import java.sql.*;

@RequiredArgsConstructor
public class SQLCore {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    private Connection connection;

    public SQLCore(String host, String database, String user, String password) {
        this(host, 3306, database, user, password);
    }

    public void connect() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password)) {
            this.connection = con;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTable(SQLTable table) {
        update("CREATE TABLE IF NOT EXISTS " + table.format());
    }

    public void update(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet query(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T queryObject(String sql, Class<T> className, String columnName) {
        try (ResultSet rs = query(sql)) {
            return rs == null ? null : rs.getObject(columnName, className);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
