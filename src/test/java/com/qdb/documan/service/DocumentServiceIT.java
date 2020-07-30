package com.qdb.documan.service;

import com.qdb.documan.DocumamagerApplication;
import com.qdb.documan.document.dto.DocumentDto;
import com.qdb.documan.document.exception.DocumentException;
import com.qdb.documan.document.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DocumamagerApplication.class)
@Transactional
public class DocumentServiceIT {

    public static final String USER_ID = "5036";
    @Autowired
    private DocumentService documentService;

    @Test
    public void shouldUploadDocument() {
        DocumentDto document = uploadDocument();

        assertThat(document).isNotNull();
        assertThat(document.getName()).isEqualTo("test.pdf");
    }

    @Test
    public void shouldFailWhenUploadingNonPDFFile() {

        String filename = "test.txt";
        MockMultipartFile file = createFile(filename);

        assertThatThrownBy(() -> documentService.upload(file, USER_ID))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("File Format not supported.");
    }

    @Test
    public void shouldViewDocumentsForUser() {
        uploadDocument();
        List<DocumentDto> documents = documentService.viewDocuments(USER_ID);

        assertThat(documents).hasSize(1);
        assertThat(documents).extractingResultOf("getName").contains("test.pdf");
    }

    @Test
    public void shouldFailToViewDocumentsForUnknownUser() {
        assertThatThrownBy(() -> documentService.viewDocuments("1111"))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("File not found.");
    }

    @Test
    public void shouldRetrieveDocumentById() {
        DocumentDto documentDto = uploadDocument();
        Resource resource = documentService.retrieveDocument(documentDto.getId());

        assertThat(resource).isNotNull();
        assertThat(resource.getFilename()).isEqualTo("test.pdf-"+ USER_ID);
    }

    @Test
    public void shouldDeleteDocument() {
        DocumentDto documentDto = uploadDocument();
         try {
             documentService.deleteDocument(USER_ID, documentDto.getName());
         }catch (Exception ex){
             fail("Wanted but could not delete the file.",ex);
         }
    }

    @Test
    public void shouldFailedToDeleteNonExistingDocument() {
        assertThatThrownBy(() -> documentService.deleteDocument(USER_ID, "test.pdf"))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("File not found.");
    }

    private DocumentDto uploadDocument() {
        String filename = "test.pdf";
        MockMultipartFile file = createFile(filename);

        return documentService.upload(file, USER_ID);
    }

    private MockMultipartFile createFile(String filename) {
        return new MockMultipartFile(
                "file",
                filename,
                MediaType.APPLICATION_PDF_VALUE,
                "<<pdf data>>".getBytes(StandardCharsets.UTF_8));
    }

}
