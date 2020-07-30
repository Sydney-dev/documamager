package com.qdb.documan.document.repository;

import com.qdb.documan.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, String> {

    Optional<Document> findDocumentByIdAndStatus(Long fileUuid, boolean status);

    Optional<Document> findDocumentByFileNameAndStatus(String fileName, boolean status);

    Optional<List<Document>> findDocumentByUserIdAndStatus(String userId, boolean status);

    Optional<Document> findDocumentByUserIdAndFileNameAndStatus(String userId, String fileName, boolean status);

}
