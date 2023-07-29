package de.fr3qu3ncy.easytools.core.sql;

import de.fr3qu3ncy.easyconfig.core.annotations.ConfigurableField;
import de.fr3qu3ncy.easyconfig.core.serialization.Configurable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ConfigurableField
@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class SQLCredentials implements Configurable<SQLCredentials> {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public static SQLCredentials defaultValues() {
        return new SQLCredentials(
            "localhost",
            3306,
            "database",
            "user",
            "password"
        );
    }
}
