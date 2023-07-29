package de.fr3qu3ncy.easytools.core.mongo;

import de.fr3qu3ncy.easyconfig.core.annotations.ConfigurableField;
import de.fr3qu3ncy.easyconfig.core.registry.ConfigRegistry;
import de.fr3qu3ncy.easyconfig.core.serialization.Configurable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ConfigurableField
@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class MongoCredentials implements Configurable<MongoCredentials> {

    static {
        ConfigRegistry.register(MongoCredentials.class);
    }

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public static MongoCredentials defaultValues() {
        return new MongoCredentials(
            "localhost",
            27017,
            "database",
            "user",
            "password"
        );
    }
}
