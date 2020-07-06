package org.pigmalion;

import io.smallrye.mutiny.Multi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    io.vertx.mutiny.pgclient.PgPool dbClient;

    @GET
    public Multi<User> getAll () {
        Multi<User> users = User.findAll(dbClient);
        return users;
    }

}
