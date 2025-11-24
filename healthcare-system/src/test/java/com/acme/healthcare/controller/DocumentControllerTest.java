package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.DocumentService;
import com.acme.healthcare.service.dto.DocumentResponse;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link DocumentController}.
 */
@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void upload_ShouldReturnDocumentMetadata() throws Exception {
        DocumentResponse response = new DocumentResponse();
        response.setId(1L);
        response.setFileName("report.txt");
        when(documentService.upload(eq(1L), isNull(), any())).thenReturn(response);

        MockMultipartFile file = new MockMultipartFile("file", "report.txt", "text/plain", "data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/documents")
                .file(file)
                .param("patientId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fileName").value("report.txt"));

        verify(documentService).upload(eq(1L), isNull(), any());
    }

    @Test
    void download_ShouldStreamFile() throws Exception {
        when(documentService.download(10L)).thenReturn(new ByteArrayInputStream("content".getBytes()));

        mockMvc.perform(get("/api/v1/documents/10"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"document_10\""))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    void listByPatient_ShouldReturnDocuments() throws Exception {
        DocumentResponse response = new DocumentResponse();
        response.setId(3L);
        when(documentService.listByPatient(7L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/documents/patient/7"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    void listByVisit_ShouldReturnDocuments() throws Exception {
        DocumentResponse response = new DocumentResponse();
        response.setId(4L);
        when(documentService.listByVisit(9L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/documents/visit/9"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/documents/6"))
            .andExpect(status().isNoContent());

        verify(documentService).delete(6L);
    }
}










