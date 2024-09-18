package org.example.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entity.User;
import org.example.interfaces.UserService;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;
    private final UserService userService;

    @Inject
    public UserResource(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GET
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(Response::ok).orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @POST
    @Transactional
    public Response createUser(User user) {
        if (user == null || user.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        userService.save(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, User updatedUser) {
        if (updatedUser == null || updatedUser.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<User> existingUserOpt = userService.findById(id);
        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();
            userService.update(user, updatedUser);
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
