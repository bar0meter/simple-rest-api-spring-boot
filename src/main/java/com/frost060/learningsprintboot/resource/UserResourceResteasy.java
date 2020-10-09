package com.frost060.learningsprintboot.resource;

import com.frost060.learningsprintboot.model.User;
import com.frost060.learningsprintboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Validated
@Component
@Path("/api/v1/users")
public class UserResourceResteasy {

    private final UserService userService;

    @Autowired
    public UserResourceResteasy(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{userId}")
    public User fetchUser(@PathParam("userId") UUID userId) {
        return userService
                .getUser(userId)
                .orElseThrow(() -> new NotFoundException("user" + userId + " not found"));
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public void insertNewUser(@Valid User user) {
        userService.insertUser(user);
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public void updateUser(@Valid User user) {
        userService.updateUser(user);
    }

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("{userId}")
    public void deleteUser(@PathParam("userId") UUID userId) {
        userService.removeUser(userId);
    }

    static class ErrorMessage {
        String errorMessage;

        public ErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
