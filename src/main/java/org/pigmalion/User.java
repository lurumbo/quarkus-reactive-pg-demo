package org.pigmalion;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import java.util.stream.StreamSupport;

public class User {

    public Long id;
    public String name;

    public User () {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Multi<User> findAll (PgPool dbClient) {
        Uni<RowSet<Row>> rowSet = dbClient.query("SELECT id, name FROM users").execute();
        Multi<User> users = rowSet
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(User::from);
        return users;
    }

    private static User from (Row row) {
        return new User(row.getLong("id"), row.getString("name"));
    }

}
