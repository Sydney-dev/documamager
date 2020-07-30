package com.qdb.documan.post.repository;

import com.qdb.documan.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<List<Post>> findPostByDocumentId(String documentId);
}
