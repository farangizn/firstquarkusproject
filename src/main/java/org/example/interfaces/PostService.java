package org.example.interfaces;

import org.example.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> findAll();

    Optional<Post> findById(Long id);

    Post save(Post post);

    void update(Post post);

    boolean deleteById(Long id);

}
