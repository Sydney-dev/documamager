package com.qdb.documan.service;

import com.qdb.documan.document.domain.Document;
import com.qdb.documan.document.dto.DocumentDto;
import com.qdb.documan.document.exception.DocumentException;
import com.qdb.documan.document.repository.DocumentRepository;
import com.qdb.documan.document.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    public static final String TEST_PDF = "test.pdf";
    public static final String USER_ID = "5";
    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldUploadNewDocument() {

        when(documentRepository.save(any(Document.class))).thenReturn(getDocument());
        DocumentDto document = documentService.upload(createFile(TEST_PDF), USER_ID);

        assertThat(document).isNotNull();
        assertThat(document.getName()).isEqualTo(TEST_PDF);

        verify(documentRepository,atLeastOnce()).save(any(Document.class));
    }

    @Test
    public void shouldUpdateExistingDocument() {

        when(documentRepository.findDocumentByFileNameAndStatus(anyString(),anyBoolean())).thenReturn(createDocument());
        when(documentRepository.save(any(Document.class))).thenReturn(getDocument());

        DocumentDto document = documentService.upload(createFile(TEST_PDF), USER_ID);

        assertThat(document).isNotNull();
        assertThat(document.getName()).isEqualTo(TEST_PDF);

        verify(documentRepository,atLeastOnce()).save(any(Document.class));
        verify(documentRepository,atLeastOnce()).findDocumentByFileNameAndStatus(anyString(), anyBoolean());
    }

    @Test
    public void shouldViewDocumentsForUser() {

        when(documentRepository.findDocumentByUserIdAndStatus(anyString(),anyBoolean())).thenReturn(getDocuments());

        List<DocumentDto> documents = documentService.viewDocuments(USER_ID);

        assertThat(documents).hasSize(1);
        assertThat(documents).extractingResultOf("getName").contains("test.pdf");
    }

    @Test
    public void shouldCatchFileNotFoundWhenViewDocuments() {
        assertThatThrownBy(() -> documentService.viewDocuments(USER_ID))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("File not found.");
    }

    @Test
    public void shouldRetrieveDocumentById() {
        when(documentRepository.findDocumentByIdAndStatus(anyLong(),anyBoolean())).thenReturn(createDocument());

        Resource resource = documentService.retrieveDocument(152L);

        assertThat(resource).isNotNull();
        assertThat(resource.getFilename()).isEqualTo("test.pdf");
    }

    @Test
    public void shouldCatchFileNotFoundWhenRetrieveDocumentById() {
        when(documentRepository.findDocumentByIdAndStatus(anyLong(),anyBoolean())).thenThrow(new RuntimeException());

        assertThatThrownBy(() -> documentService.retrieveDocument(152L))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("File not found.");
    }

    private MockMultipartFile createFile(String filename) {
        return new MockMultipartFile(
                "file",
                filename,
                MediaType.APPLICATION_PDF_VALUE,
                "<<pdf data>>".getBytes(StandardCharsets.UTF_8));
    }


    private Optional<List<Document>> getDocuments() {
        List<Document> documents = Arrays.asList(getDocument());
        return Optional.of(documents);
    }

    private Optional<Document> createDocument() {
        Document doc = getDocument();
        return Optional.of(doc);
    }

    private Document getDocument() {
        Document doc = new Document();
        doc.setId(152L);
        doc.setStatus(true);
        doc.setFileName(TEST_PDF);
        return doc;
    }

}
