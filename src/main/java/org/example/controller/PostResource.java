package org.example.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entity.Post;
import org.example.interfaces.PostService;

import java.util.List;
import java.util.Optional;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    private final PostService postService;

    @Inject
    public PostResource(PostService postService) {
        this.postService = postService;
    }

    @GET
    public Response getAllPosts() {
        List<Post> posts = postService.findAll();
        return Response.ok(posts).build();
    }

    @GET
    @Path("/{id}")
    public Response getPostById(@PathParam("id") Long id) {
        Optional<Post> post = postService.findById(id);
        return post.map(Response::ok).orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Post not found for ID: " + id)).build();
    }

    @POST
    @Transactional
    public Response createPost(Post post) {
        if (post == null || post.getTitle() == null || post.getBody() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Post title and body must not be null")
                    .build();
        }

        Post createdPost = postService.save(post);
        return Response.status(Response.Status.CREATED).entity(createdPost).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePost(@PathParam("id") Long id, Post updatedPost) {
        if (updatedPost == null || updatedPost.getTitle() == null || updatedPost.getBody() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Post title and body must not be null")
                    .build();
        }

        Optional<Post> existingPostOpt = postService.findById(id);
        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();
            postService.update(existingPost);
            return Response.ok(existingPost).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Post not found for ID: " + id)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePost(@PathParam("id") Long id) {
        boolean deleted = postService.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Post not found for ID: " + id)
                    .build();
        }
    }
}
