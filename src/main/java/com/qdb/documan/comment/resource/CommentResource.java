package com.qdb.documan.comment.resource;

import com.qdb.documan.comment.client.CommentClient;
import com.qdb.documan.comment.dto.CommentDto;
import com.qdb.documan.comment.service.CommentService;
import com.qdb.documan.post.service.PostService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentResource {
    private final CommentClient commentClient;
    private final CommentService commentService;

    public CommentResource(CommentClient commentClient, CommentService commentService) {
        this.commentClient = commentClient;
        this.commentService = commentService;
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<List<CommentDto>> findUserComments(@PathVariable("postId") long postId) {
        List<CommentDto> comments = commentClient.findUserComments(postId);
        if (CollectionUtils.isEmpty(comments)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PostService.POST_NOT_FOUND);
        }
        return ResponseEntity.ok(commentService.saveComments(comments));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<CommentDto> userComment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.findComment(id));
    }

}
