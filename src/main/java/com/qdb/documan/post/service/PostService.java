package com.qdb.documan.post.service;

import com.qdb.documan.post.client.PostClient;
import com.qdb.documan.post.domain.Post;
import com.qdb.documan.post.dto.PostDto;
import com.qdb.documan.post.exception.PostException;
import com.qdb.documan.post.repository.PostRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {

    public static final String POST_NOT_FOUND = "Post not found";
    private PostRepository postRepository;
    private PostClient postClient;
    private ModelMapper modelMapper;

    public PostService(PostRepository postRepository, PostClient postClient, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.postClient = postClient;
        this.modelMapper = modelMapper;
    }

    public List<PostDto> savePosts(String documentId, Long userId) {
        List<PostDto> userPosts = postClient.findUserPosts(userId);

        if (CollectionUtils.isEmpty(userPosts)) {
           throw new PostException(POST_NOT_FOUND);
        }

        List<Post> posts = postRepository.saveAll(mapPosts(userPosts, documentId));
        return toDto(posts);
    }

    public List<PostDto> findPostsUsingDocument(String documentId) {
        List<Post> posts = postRepository.findPostByDocumentId(documentId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        return posts.stream().map(
                p -> new PostDto(p.getId(), p.getUserId(), p.getTitle(), p.getBody(), documentId))
                .collect(Collectors.toList());
    }

    public PostDto findPost(Long id) {
        Post post = postRepository.getOne(id);
        if (post == null) {
            throw new PostException(POST_NOT_FOUND);
        }
        return modelMapper.map(post, PostDto.class);
    }

    private List<Post> mapPosts(List<PostDto> postDtos, String documentId) {
        return postDtos
                .stream()
                .map(dto -> {
                    Post post = modelMapper.map(dto, Post.class);
                    post.setDocumentId(documentId);
                    return post;
                })
                .collect(Collectors.toList());
    }

    private List<PostDto> toDto(List<Post> posts) {
        return posts
                .stream()
                .map(dto -> modelMapper.map(dto, PostDto.class))
                .collect(Collectors.toList());
    }
}
