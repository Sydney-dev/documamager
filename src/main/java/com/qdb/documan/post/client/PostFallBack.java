package com.qdb.documan.post.client;

import com.qdb.documan.post.dto.PostDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PostFallBack  implements PostClient {

	@Override
	public List<PostDto> findUserPosts(long userId) {
		return Collections.emptyList();
	}
}
