package de.fr3qu3ncy.easytools.core.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnection {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    private HikariDataSource dataSource;

    public SQLConnection(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        setup();
    }

    public SQLConnection(String host, String database, String user, String password) {
        this(host, 3306, database, user, password);
    }

    private void setup() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);

        this.dataSource = new HikariDataSource(config);
    }

    public Connection establishConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
