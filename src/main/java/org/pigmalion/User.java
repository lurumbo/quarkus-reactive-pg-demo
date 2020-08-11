package org.pigmalion;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class User {

    public Long id;
    public String name;

    public User () {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private static Logger logger = LoggerFactory.getLogger(User.class);

    private static User from (Row row) {
        return new User(row.getLong("id"), row.getString("name"));
    }

    public static Multi<User> findAll (PgPool client) {
      return client
                .query("SELECT id, name FROM users")
                .execute()
                .onItem().produceMulti(set -> Multi.createFrom().iterable(set))
                .onItem().apply(User::from);
    }

    public static Uni<List<User>> findAllUni (PgPool client) {
      String rawQuery = "SELECT id, name FROM users";
      Uni<RowSet<Row>> rowSet = client.query(rawQuery).execute();
      return rowSet.onItem().apply(pgRowSet -> {
          List<User> usersList = new ArrayList<>(pgRowSet.size());
          for (Row row : pgRowSet) {
            usersList.add(from(row));
          }
          return usersList;
        });
    }

}
