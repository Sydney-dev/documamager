package com.qdb.documan.post.client;

import com.qdb.documan.post.dto.PostDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "postClient", url = "http://jsonplaceholder.typicode.com/", fallback = PostFallBack.class)
public interface PostClient {
	@GetMapping(value = "/users/{userId}/posts")
	@Headers("Content-Type: application/json")
	List<PostDto> findUserPosts(@PathVariable("userId") long userId);
	
}
