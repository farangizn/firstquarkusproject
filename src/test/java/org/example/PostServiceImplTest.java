package org.example;

@QuarkusTest
public class PostServiceImplTest {

    @Inject
    PostServiceImpl postService;

    @InjectMock
    PostRepository postRepository;

    @InjectMock
    UserService userService;

    @Test
    public void testFindAllPosts() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.listAll()).thenReturn(List.of(post));

        List<Post> posts = postService.findAll();

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Test Title", posts.get(0).getTitle());
        verify(postRepository, times(1)).listAll();
    }

    @Test
    public void testFindPostById_Found() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));

        Optional<Post> foundPost = postService.findById(1L);

        assertTrue(foundPost.isPresent());
        assertEquals("Test Title", foundPost.get().getTitle());
        verify(postRepository, times(1)).findByIdOptional(1L);
    }

    @Test
    public void testFindPostById_NotFound() {
        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Optional<Post> foundPost = postService.findById(1L);

        assertFalse(foundPost.isPresent());
        verify(postRepository, times(1)).findByIdOptional(1L);
    }

    @Test
    public void testSavePost_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        PostInputPTO postInputPTO = new PostInputPTO();
        postInputPTO.setTitle("Test Title");
        postInputPTO.setBody("Test Body");
        postInputPTO.setUserId(1L);

        when(userService.findById(1L)).thenReturn(Optional.of(user));

        Post post = new Post();
        post.setTitle(postInputPTO.getTitle());
        post.setBody(postInputPTO.getBody());
        post.setUser(user);

        Post savedPost = postService.save(postInputPTO);

        assertNotNull(savedPost);
        assertEquals("Test Title", savedPost.getTitle());
        verify(userService, times(1)).findById(1L);
        verify(postRepository, times(1)).persist(savedPost);
    }

    @Test
    public void testSavePost_UserNotFound() {
        PostInputPTO postInputPTO = new PostInputPTO();
        postInputPTO.setTitle("Test Title");
        postInputPTO.setBody("Test Body");
        postInputPTO.setUserId(1L);

        when(userService.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.save(postInputPTO));
        assertEquals("User with ID 1 not found.", exception.getMessage());
        verify(userService, times(1)).findById(1L);
        verify(postRepository, never()).persist(any(Post.class));
    }

    @Test
    public void testUpdatePost_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Old Title");
        post.setBody("Old Body");
        post.setUser(user);

        PostInputPTO postInputPTO = new PostInputPTO();
        postInputPTO.setTitle("New Title");
        postInputPTO.setBody("New Body");
        postInputPTO.setUserId(1L);

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        postService.update(post, postInputPTO);

        assertEquals("New Title", post.getTitle());
        assertEquals("New Body", post.getBody());
        verify(postRepository, times(1)).persist(post);
    }

    @Test
    public void testUpdatePost_UserNotFound() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Old Title");
        post.setBody("Old Body");
        PostInputPTO postInputPTO = new PostInputPTO();
        postInputPTO.setTitle("New Title");
        postInputPTO.setBody("New Body");
        postInputPTO.setUserId(1L);

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));
        when(userService.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.update(post, postInputPTO));
        assertEquals("User not found", exception.getMessage());
        verify(postRepository, never()).persist(post);
    }

    @Test
    public void testDeletePost_Success() {
        when(postRepository.deleteById(1L)).thenReturn(true);

        Boolean deleted = postService.deleteById(1L);

        assertTrue(deleted);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePost_NotFound() {
        when(postRepository.deleteById(1L)).thenReturn(false);

        Boolean deleted = postService.deleteById(1L);

        assertFalse(deleted);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAllPostsByUserId() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.findAllByUserId(1L)).thenReturn(List.of(post));

        List<Post> posts = postService.findAllByUserId(1L);

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Test Title", posts.get(0).getTitle());
        verify(postRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    public void testDeleteAllPostsByUserId() {
        postService.deleteAllByUserId(1L);

        verify(postRepository, times(1)).deleteAllByUserId(1L);
    }
}