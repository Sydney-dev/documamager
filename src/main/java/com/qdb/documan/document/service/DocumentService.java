package com.qdb.documan.document.service;

import com.qdb.documan.document.domain.Document;
import com.qdb.documan.document.dto.DocumentDto;
import com.qdb.documan.document.exception.DocumentException;
import com.qdb.documan.document.repository.DocumentRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    public static final String EXTENSION = ".pdf";
    public static final String CONTENT_TYPE = "application/pdf";
    public static final String FILE_NOT_FOUND = "File not found.";
    private DocumentRepository documentRepository;
    private Path documentLocation;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        createDocumentLocation();
    }

    private void createDocumentLocation() {
        String uploadDirectory = System.getProperty("user.home");
        this.documentLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.documentLocation);
        } catch (Exception ex) {
            throw new DocumentException("Error Creating document location");
        }
    }

    public DocumentDto upload(MultipartFile file, String useId) {
        validateFile(file);
        String fileName = file.getOriginalFilename() + "-" + useId;

        Path targetLocation = this.documentLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DocumentException("Error saving Document.");
        }

        String cleanFileName = StringUtils.cleanPath(file.getOriginalFilename());
        Document document = this.saveDocument(fileName, useId, true);
        String documentUri = getDocumentUri(document.getId());
        return new DocumentDto(cleanFileName, documentUri, document.getId());
    }

    public List<DocumentDto> viewDocuments(String userId) {
        List<Document> documents = documentRepository
                .findDocumentByUserIdAndStatus(userId, true)
                .orElseThrow(() -> new DocumentException(FILE_NOT_FOUND));

        return documents.stream()
                .map(document -> {
                    Long id = document.getId();
                    return new DocumentDto(removeUserId(document.getFileName(), userId),
                            getDocumentUri(id), id);
                })
                .collect(Collectors.toList());
    }

    public Resource retrieveDocument(Long documentId) {
        try {
            Document document = documentRepository.findDocumentByIdAndStatus(documentId, true)
                    .orElseThrow(() -> new DocumentException(FILE_NOT_FOUND));
            Path filePath = this.documentLocation.resolve(document.getFileName()).normalize();
            return new UrlResource(filePath.toUri());
        } catch (Exception ex) {
            throw new DocumentException(FILE_NOT_FOUND);
        }
    }

    public void deleteDocument(String userId, String documentName) {
        try {
            documentName = documentName + "-" + userId;
            Document document = documentRepository.
                    findDocumentByUserIdAndFileNameAndStatus(userId, documentName, true)
                    .orElseThrow(() -> new DocumentException(FILE_NOT_FOUND));

            Path location = this.documentLocation.resolve(document.getFileName());
            Files.delete(location);
            document.setStatus(false);
            documentRepository.save(document);
        } catch (IOException e) {
            throw new DocumentException(FILE_NOT_FOUND);
        }
    }

    //  <--- Steam methods --->
    private String getDocumentUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/document/download/")
                .path(String.valueOf(id)).toUriString();
    }

    private void validateFile(MultipartFile file) {
        String fileName = org.apache.commons.lang3.StringUtils.lowerCase(
                StringUtils.cleanPath(file.getOriginalFilename()));
        if (!fileName.contains(EXTENSION) || !file.getContentType().equals(CONTENT_TYPE)) {
            throw new DocumentException("File Format not supported.");
        }
    }

    protected Document saveDocument(String fileName, String userId, boolean status) {
        Document doc = findDocument(fileName, status);
        Document document;
        if (Objects.nonNull(doc)) {
            document = doc;
            document.setStatus(status);
        } else {
            String fileLocation = this.documentLocation.resolve(fileName).toString();
            document = new Document( fileName,
                    new java.sql.Date(System.currentTimeMillis()), fileLocation, userId, status);
        }
        return documentRepository.save(document);
    }

    private Document findDocument(String fileName, boolean status) {
        return documentRepository.findDocumentByFileNameAndStatus(fileName, status).orElse(null);
    }

    private String removeUserId(String fileName, String userId) {
        return org.apache.commons.lang3.StringUtils.substringBefore(fileName, "-" + userId);
    }
}
