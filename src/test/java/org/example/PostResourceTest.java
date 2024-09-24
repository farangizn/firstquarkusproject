package org.example;

@QuarkusTest
class PostResourceTest {

    @InjectMock
    PostRepository postRepository;

    @InjectMock
    PostResource postResource;

    private Post post;
    private PostInputPTO postInputPTO;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setTitle("Hello World");
        post.setBody("Hello from Quarkus to the World!");

        postInputPTO = new PostInputPTO();
        postInputPTO.setUserId(1L);
        postInputPTO.setTitle("Hello World");
        postInputPTO.setBody("Hello from Quarkus to the World!");
    }

    @Test
    void getAllPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        Mockito.when(postRepository.listAll()).thenReturn(posts);
        Response response = postResource.getAllPosts();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        List<PostDTO> entity = (List<PostDTO>) response.getEntity();
        assertFalse(entity.isEmpty());
        assertEquals(1L, entity.get(0).getId());
        assertEquals("Hello World", entity.get(0).getTitle());
        assertEquals("Hello from Quarkus to the World!", entity.get(0).getBody());
    }

    @Test
    void getPostById() {
        Mockito.when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        Response response = postResource.getPostById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        PostDTO entity = (PostDTO) response.getEntity();
        assertEquals(1L, entity.getId());
        assertEquals("Hello World", entity.getTitle());
        assertEquals("Hello from Quarkus to the World!", entity.getBody());
    }

    @Test
    void getPostByIdNotFound() {
        Mockito.when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.empty());

        Response response = postResource.getPostById(1L);
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    void createPost() {

        Mockito.doNothing().when(postRepository).persist(
                ArgumentMatchers.any(Post.class)
        );

        PostInputPTO postInputPTO = new PostInputPTO("new title", "new body", 1L);
        Response response = postResource.createPost(postInputPTO);
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity()); // maybe will be removed
    }

    @Test
    void createPostBadRequest() {

        Mockito.doNothing().when(postRepository).persist(
                ArgumentMatchers.any(Post.class)
        );

        PostInputPTO postInputPTO = new PostInputPTO("new title", "new body", 1L);
        Response response = postResource.createPost(postInputPTO);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());
    }

    @Test
    void updatePost() {

        postInputPTO.setTitle("updated title");

        Mockito.doNothing().when(postRepository).persist(ArgumentMatchers.any(Post.class));

        Mockito.when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        Response response = postResource.updatePost(1L, postInputPTO);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        PostDTO entity = (PostDTO) response.getEntity();
        assertEquals(1L, entity.getId());
        assertEquals("updated title", entity.getTitle());
        assertEquals("Hello from Quarkus to the World!", entity.getBody());
    }

    @Test
    void updatePostNotFound() {

        Mockito.doNothing().when(postRepository).persist(ArgumentMatchers.any(Post.class));
        Mockito.when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        Response response = postResource.updatePost(1L, postInputPTO);

        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void deletePost() {

        Mockito.when(postRepository.deleteById(1L)).thenReturn(true);

        Response response = postResource.deletePost(1L);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    void deletePostNotFound() {

        Mockito.when(postRepository.deleteById(1L)).thenReturn(true);

        Response response = postResource.deletePost(1L);
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    void getPostsByUserId() {
        Mockito.when(postRepository.findAllByUserId(1L)).thenReturn(List.of(post));
        Response response = postResource.getPostsByUserId(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        List<PostDTO> entity = (List<PostDTO>) response.getEntity();
        assertFalse(entity.isEmpty());
        assertEquals(1L, entity.get(0).getId());
    }

    @Test
    void deletePostsByUserId() {
        Mockito.when(postRepository.findAllByUserId(1L)).thenReturn(List.of());
        Response response = postResource.deletePostsByUserId(1L);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}