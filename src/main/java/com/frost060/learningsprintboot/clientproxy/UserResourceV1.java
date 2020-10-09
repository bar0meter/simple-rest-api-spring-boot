package com.frost060.learningsprintboot.clientproxy;

import com.frost060.learningsprintboot.model.User;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface UserResourceV1 {

    @GET
    @Produces(APPLICATION_JSON)
    List<User> fetchUsers(@QueryParam("gender") String gender);

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{userId}")
    User fetchUser(@PathParam("userId") UUID userId);

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    void insertNewUser(@RequestBody User user);

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    void updateUser(@RequestBody User user);

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("{userId}")
    void deleteUser(@PathParam("userId") UUID userId);
}
