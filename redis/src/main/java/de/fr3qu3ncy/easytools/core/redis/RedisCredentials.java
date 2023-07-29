package de.fr3qu3ncy.easytools.core.redis;

import de.fr3qu3ncy.easyconfig.core.annotations.ConfigurableField;
import de.fr3qu3ncy.easyconfig.core.registry.ConfigRegistry;
import de.fr3qu3ncy.easyconfig.core.serialization.Configurable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ConfigurableField
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedisCredentials implements Configurable<RedisCredentials> {

    static {
        ConfigRegistry.register(RedisCredentials.class);
    }

    private String host;
    private int port;
    private String password;

    public static RedisCredentials defaultValues() {
        return new RedisCredentials("localhost", 6379, "");
    }

}