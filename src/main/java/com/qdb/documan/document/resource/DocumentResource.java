package com.qdb.documan.document.resource;

import com.qdb.documan.document.dto.DocumentDto;
import com.qdb.documan.document.exception.DocumentException;
import com.qdb.documan.document.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/document")
public class DocumentResource {

    private static final Logger logger = LoggerFactory.getLogger(DocumentResource.class);
    private DocumentService documentService;

    public DocumentResource(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DocumentDto> uploadDocument(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") String userId) {

        return ResponseEntity.ok(documentService.upload(file, userId));

    }

    @GetMapping(value = "/view/user/{userId}")
    public ResponseEntity<List<DocumentDto>> viewDocuments(@PathVariable("userId") String userId) {
        try{
            List<DocumentDto> documents = documentService.viewDocuments(userId);
            return ResponseEntity.ok(documents);
        } catch (DocumentException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id,
                                             HttpServletRequest request) throws IOException {
        Resource resource = documentService.retrieveDocument(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = DocumentService.CONTENT_TYPE;
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFile().getName() + "\"")
                .body(resource);

    }

    @DeleteMapping(value = "/delete/documentName/{documentName}/user/{userId}")
    public void deleteDocument(@PathVariable("documentName") String documentName,
                               @PathVariable("userId") String userId) {
        documentService.deleteDocument(userId, documentName);
    }
}
