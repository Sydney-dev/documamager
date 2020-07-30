package com.qdb.documan.comment.client;

import com.qdb.documan.comment.dto.CommentDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CommentFallBack implements CommentClient{

	@Override
	public List<CommentDto> findUserComments(long postId) {
		return Collections.emptyList();
	}

}
