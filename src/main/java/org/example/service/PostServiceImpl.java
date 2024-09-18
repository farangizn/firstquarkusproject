package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.entity.Post;
import org.example.interfaces.PostService;
import org.example.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Inject
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.listAll();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(postRepository.findById(id));
    }

    @Override
    @Transactional
    public Post save(Post post) {
        postRepository.persist(post);
        return post;
    }

    @Override
    @Transactional
    public void update(Post post) {
        post.setTitle(post.getTitle());
        post.setBody(post.getBody());
        postRepository.persist(post);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return postRepository.deleteById(id);
    }
}
