package org.pigmalion;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    io.vertx.mutiny.pgclient.PgPool dbClient;

    private Logger logger = LoggerFactory.getLogger(UsersResource.class);

    @GET
    public Multi<User> getAll () {
        logger.info("GET /users");
        Multi<User> users = User.findAll(dbClient);
        return users;
    }

    // @GET
    public String getString () {
      return "Hello World";
    }

    // @GET
    public Uni<String> getUniString () {
      String label = "Hello World with Uni";
      return Uni.createFrom().item(label).onItem().apply(lbl -> lbl);
    }

    // @GET
    public Multi<String> getMultiString () {
      return Multi
        .createFrom()
        .items("foo", "bar", "baz")
        .onItem()
        .apply(s -> s);
    }

    // @GET
    public Uni<Response> getUniResponse () {
      System.out.println("Getting results users");
      return User.findAllUni(dbClient)
        .onItem().apply(Response::ok)
        .onItem().apply(Response.ResponseBuilder::build);
    }

}
