package com.qdb.documan.comment.client;

import com.qdb.documan.comment.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "commentClient", url = "http://jsonplaceholder.typicode.com/", fallback = CommentFallBack.class)
public interface CommentClient {

    @GetMapping(value = "/post/{postId}/comments")
    List<CommentDto> findUserComments(@PathVariable("postId") long postId);
}
