package de.fr3qu3ncy.easytools.core.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.fr3qu3ncy.easyconfig.core.registry.ConfigRegistry;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnection {

    static {
        ConfigRegistry.register(SQLCredentials.class);
    }

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

    public SQLConnection(SQLCredentials credentials) {
        this(credentials.getHost(), credentials.getPort(), credentials.getDatabase(),
            credentials.getUser(), credentials.getPassword());
    }

    private void setup() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);

        this.dataSource = new HikariDataSource(config);
    }

    public Connection establishConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
