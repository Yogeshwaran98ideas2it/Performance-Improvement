package com.acme.healthcare.service.impl;

import com.acme.healthcare.config.DocumentStorageProperties;
import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.repository.DocumentRepository;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.mapper.DocumentMapper;
import com.acme.healthcare.service.dto.DocumentResponse;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DocumentServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @TempDir
    Path tempDir;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private DocumentMapper documentMapper;

    private DocumentServiceImpl service;

    @BeforeEach
    void setUp() throws IOException {
        DocumentStorageProperties properties = new DocumentStorageProperties();
        properties.setRoot(tempDir.toString());
        service = new DocumentServiceImpl(
            documentRepository,
            patientRepository,
            visitRepository,
            documentMapper,
            properties
        );
    }

    @Test
    void upload_WithVisit_ShouldPersistDocumentAndStoreFile() {
        Patient patient = new Patient();
        patient.setId(1L);
        PatientVisit visit = new PatientVisit();
        visit.setId(2L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(2L)).thenReturn(Optional.of(visit));

        DocumentResponse response = new DocumentResponse();
        response.setId(42L);
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(documentMapper.toResponse(any(Document.class))).thenReturn(response);

        MultipartFile multipartFile = new MockMultipartFile(
            "file",
            "report.pdf",
            "application/pdf",
            "sample data".getBytes(StandardCharsets.UTF_8)
        );

        DocumentResponse result = service.upload(1L, 2L, multipartFile);

        assertThat(result).isSameAs(response);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(captor.capture());
        Document persisted = captor.getValue();

        assertEquals(patient, persisted.getPatient());
        assertEquals(visit, persisted.getVisit());
        assertEquals("report.pdf", persisted.getFileName());
        assertEquals("application/pdf", persisted.getFileType());
        assertEquals(multipartFile.getSize(), persisted.getFileSize());
        assertNotNull(persisted.getChecksum());
        assertThat(persisted.getStoragePath()).contains("patients/1/visits/2");
        assertThat(Files.exists(Path.of(persisted.getStoragePath()))).isTrue();
    }

    @Test
    void upload_WithoutVisit_ShouldStoreUnderPatientDocuments() {
        Patient patient = new Patient();
        patient.setId(10L);
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));

        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DocumentResponse response = new DocumentResponse();
        when(documentMapper.toResponse(any(Document.class))).thenReturn(response);

        MultipartFile multipartFile = new MockMultipartFile(
            "file",
            "scan.png",
            "image/png",
            "png-data".getBytes(StandardCharsets.UTF_8)
        );

        service.upload(10L, null, multipartFile);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(captor.capture());
        Document persisted = captor.getValue();

        assertThat(persisted.getStoragePath()).contains("patients/10/documents");
    }

    @Test
    void upload_WhenPatientMissing_ShouldThrow() {
        when(patientRepository.findById(5L)).thenReturn(Optional.empty());

        MultipartFile multipartFile = new MockMultipartFile(
            "file",
            "missing.txt",
            "text/plain",
            "content".getBytes(StandardCharsets.UTF_8)
        );

        assertThrows(EntityNotFoundException.class, () -> service.upload(5L, null, multipartFile));
        verify(documentRepository, never()).save(any());
    }

    @Test
    void upload_WhenVisitMissing_ShouldThrow() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(9L)).thenReturn(Optional.empty());

        MultipartFile multipartFile = new MockMultipartFile(
            "file",
            "visit.txt",
            "text/plain",
            "content".getBytes(StandardCharsets.UTF_8)
        );

        assertThrows(EntityNotFoundException.class, () -> service.upload(1L, 9L, multipartFile));
        verify(documentRepository, never()).save(any());
    }

    @Test
    void download_ShouldReturnFileStream() throws IOException {
        Path storedFile = Files.writeString(tempDir.resolve("stored.txt"), "stored-content", StandardCharsets.UTF_8);
        Document document = new Document();
        document.setId(7L);
        document.setStoragePath(storedFile.toString());
        when(documentRepository.findById(7L)).thenReturn(Optional.of(document));

        try (InputStream stream = service.download(7L)) {
            byte[] bytes = stream.readAllBytes();
            assertArrayEquals("stored-content".getBytes(StandardCharsets.UTF_8), bytes);
        }
    }

    @Test
    void download_WhenDocumentMissing_ShouldThrow() {
        when(documentRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.download(77L));
    }

    @Test
    void listByPatient_ShouldMapResponses() {
        Document document = new Document();
        document.setId(1L);
        DocumentResponse response = new DocumentResponse();
        response.setId(1L);

        when(documentRepository.findByPatientId(99L)).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<DocumentResponse> results = service.listByPatient(99L);

        assertThat(results).containsExactly(response);
    }

    @Test
    void listByVisit_ShouldMapResponses() {
        Document document = new Document();
        document.setId(2L);
        DocumentResponse response = new DocumentResponse();
        response.setId(2L);

        when(documentRepository.findByVisitId(55L)).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<DocumentResponse> results = service.listByVisit(55L);

        assertThat(results).containsExactly(response);
    }

    @Test
    void delete_ShouldRemoveMetadataAndFile() throws IOException {
        Path storedFile = Files.writeString(tempDir.resolve("delete-me.txt"), "to-delete", StandardCharsets.UTF_8);
        Document document = new Document();
        document.setId(3L);
        document.setStoragePath(storedFile.toString());

        when(documentRepository.findById(3L)).thenReturn(Optional.of(document));

        service.delete(3L);

        verify(documentRepository).delete(document);
        assertThat(Files.exists(storedFile)).isFalse();
    }

    @Test
    void delete_WhenDocumentMissing_ShouldThrow() {
        when(documentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.delete(404L));
    }
}











