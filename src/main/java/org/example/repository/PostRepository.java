package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.entity.Post;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
