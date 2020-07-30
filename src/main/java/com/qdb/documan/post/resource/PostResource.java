package com.qdb.documan.post.resource;

import com.qdb.documan.post.dto.PostDto;
import com.qdb.documan.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostResource {
    private final PostService postService;

    public PostResource(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/user/{userId}/document/{documentId}")
    public ResponseEntity<List<PostDto>> findUserPost(@PathVariable("userId") long userId,
                                                      @PathVariable("documentId") String documentId) {
        return ResponseEntity.ok(postService.savePosts(documentId, userId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> userPost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.findPost(id));
    }

    @GetMapping(value = "/document/{documentId}")
    public ResponseEntity<List<PostDto>> documentPosts(@PathVariable("documentId") String documentId) {
        return  ResponseEntity.ok(postService.findPostsUsingDocument(documentId));
    }
}
