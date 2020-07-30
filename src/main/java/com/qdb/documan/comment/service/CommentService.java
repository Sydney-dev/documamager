package com.qdb.documan.comment.service;

import com.qdb.documan.comment.domain.Comment;
import com.qdb.documan.comment.dto.CommentDto;
import com.qdb.documan.comment.exception.CommentException;
import com.qdb.documan.comment.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

	public final static String COMMENT_NOT_FOUND = "Comment not found.";
    private final CommentRepository commentRepository;
	private ModelMapper modelMapper;

	public CommentService(CommentRepository commentRepository, ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.commentRepository = commentRepository;
	}

	public List<CommentDto> saveComments(List<CommentDto> commentDTOs) {
		List<Comment> commentsSaved = commentRepository.saveAll(mapComments(commentDTOs));
		return toDto(commentsSaved);
	}

	public List<CommentDto> findComments(long postId) {
        Optional<List<Comment>> comments = commentRepository.findCommentByPostId(postId);
        if (comments.isPresent()) {
            return comments.get().stream().map(c -> new CommentDto( postId, c.getId(), c.getName(), c.getEmail(), c.getBody())).collect(Collectors.toList());
        } else {
            throw new CommentException(COMMENT_NOT_FOUND);
        }
    }

    public CommentDto findComment(Long commentUuid) {
        Comment comment = commentRepository.getOne(commentUuid);
        if (comment == null) {
            throw new CommentException(COMMENT_NOT_FOUND);
        }
        return modelMapper.map(comment, CommentDto.class);
    }


	private List<CommentDto> toDto(List<Comment> commentsSaved) {
		return commentsSaved
				.stream()
				.map(dto -> modelMapper.map(dto, CommentDto.class))
				.collect(Collectors.toList());
	}

	private List<Comment> mapComments(List<CommentDto> commentDTOs) {
		return commentDTOs
				.stream()
				.map(dto -> modelMapper.map(dto, Comment.class))
				.collect(Collectors.toList());
	}
}
