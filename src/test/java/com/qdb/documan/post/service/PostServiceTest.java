package com.qdb.documan.post.service;

import com.qdb.documan.post.client.PostClient;
import com.qdb.documan.post.domain.Post;
import com.qdb.documan.post.dto.PostDto;
import com.qdb.documan.post.repository.PostRepository;
import com.qdb.documan.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    public static final long USER_ID = 5L;
    private PostService postService;
    private PostRepository postRepository = mock(PostRepository.class);
    private PostClient postClient = mock(PostClient.class);

    @BeforeEach
    void setUp() {
        postService = new PostService( postRepository, postClient, new ModelMapper());
    }

    @Test
    void shouldSavePost() {
        when(postClient.findUserPosts(Mockito.anyLong())).thenReturn(createPostDtos());
        when(postRepository.saveAll(anyList())).thenReturn(createPosts());
        List<PostDto> postDtos = postService.savePosts("150", 15L);
        assertThat(postDtos).hasSize(1);
    }


    @Test
    void findPostsUsingDocument() {

        when(postRepository.findPostByDocumentId(anyString())).thenReturn(getPosts());

        List<PostDto> postDtos = postService.findPostsUsingDocument("1");

        assertThat(postDtos).hasSize(1);
        assertThat(postDtos.get(0).getId()).isEqualTo(5L);
        assertThat(postDtos.get(0).getUserId()).isEqualTo(USER_ID);
    }

    private List<Post> createPosts() {
        return Arrays.asList(createPost());
    }

    private Post createPost() {
        Post post = new Post();
        post.setId(5L);
        post.setUserId(USER_ID);
        return post;
    }


    private List<PostDto> createPostDtos() {
        return Arrays.asList(createPostDto());
    }

    private PostDto createPostDto() {
        PostDto postDto = new PostDto();
        postDto.setUserId(USER_ID);
        return postDto;
    }

    private Optional<List<Post>> getPosts() {
        return Optional.of(createPosts());
    }
}
