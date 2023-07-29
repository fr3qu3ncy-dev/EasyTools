package de.fr3qu3ncy.easytools.core.mongo;


import com.mongodb.MongoSecurityException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.filters.Filter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MongoConnection {

    @Getter
    private final Datastore datastore;

    private final MongoClient mongoClient;

    private final List<Class<?>> entities = new ArrayList<>();

    public MongoConnection(MongoCredentials credentials, Class<?>... entities)
        throws MongoSecurityException, MongoSocketOpenException {
        String uri = "mongodb://" +
            (!credentials.getUser().isBlank() ?
                credentials.getUser() + ":" + credentials.getPassword() + "@" : "") +
            credentials.getHost() + ":" + credentials.getPort();

        this.entities.addAll(List.of(entities));
        this.mongoClient = MongoClients.create(uri);
        this.datastore = Morphia.createDatastore(mongoClient, credentials.getDatabase());
    }

    public void load() {
        datastore.getMapper().map(new ArrayList<>(entities));
        datastore.ensureIndexes();
    }

    public <T> void save(T object) {
        datastore.save(object);
    }

    public <T> void delete(T object) {
        datastore.delete(object);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return datastore.find(clazz).stream().toList();
    }

    public <T> List<T> findAll(Class<T> clazz, Filter... filters) {
        return datastore.find(clazz).filter(filters).stream().toList();
    }

    public <T> T find(Class<T> clazz) {
        return datastore.find(clazz).first();
    }

    public <T> T find(Class<T> clazz, Filter... filters) {
        return datastore.find(clazz).filter(filters).first();
    }

    public boolean isConnected() {
        return mongoClient.getClusterDescription().hasWritableServer();
    }
}
