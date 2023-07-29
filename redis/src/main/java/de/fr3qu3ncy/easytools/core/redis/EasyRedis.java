package de.fr3qu3ncy.easytools.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class EasyRedis {

    private EasyRedis() {}

    private static JedisPool pool;
    private static RedisCredentials credentials;

    public static void init(RedisCredentials credentials) {
        EasyRedis.credentials = credentials;
        pool = new JedisPool(credentials.getHost(), credentials.getPort());
    }

    public static void set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            if (credentials.getPassword() != null && !credentials.getPassword().isBlank()) jedis.auth(credentials.getPassword());
            jedis.set(key, value);
        }
    }

    public static String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            if (credentials.getPassword() != null && !credentials.getPassword().isBlank()) jedis.auth(credentials.getPassword());
            return jedis.get(key);
        }
    }

    public static void close() {
        if (pool != null) pool.close();
    }
}
