package com.acme.healthcare.service.impl;

import com.acme.healthcare.config.DocumentStorageProperties;
import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.repository.DocumentRepository;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.mapper.DocumentMapper;
import com.acme.healthcare.service.DocumentService;
import com.acme.healthcare.service.dto.DocumentResponse;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * DocumentServiceImpl manages file storage and metadata.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final PatientVisitRepository visitRepository;
    private final DocumentMapper documentMapper;
    private final Path rootDirectory;

    /**
     * Creates the service.
     *
     * @param documentRepository the document repository
     * @param patientRepository the patient repository
     * @param visitRepository the visit repository
     * @param documentMapper the document mapper
     * @param properties the storage properties
     * @throws IOException if the storage path cannot be created
     */
    public DocumentServiceImpl(final DocumentRepository documentRepository,
                               final PatientRepository patientRepository,
                               final PatientVisitRepository visitRepository,
                               final DocumentMapper documentMapper,
                               final DocumentStorageProperties properties) throws IOException {
        this.documentRepository = documentRepository;
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
        this.documentMapper = documentMapper;
        this.rootDirectory = Path.of(properties.getRoot()).toAbsolutePath().normalize();
        Files.createDirectories(this.rootDirectory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentResponse upload(final Long patientId, final Long visitId, final MultipartFile file) {
        try {
            Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
            PatientVisit visit = null;
            if (visitId != null) {
                visit = visitRepository.findById(visitId)
                    .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
            }
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            Path storagePath = resolveStoragePath(patientId, visitId, originalName);
            Files.createDirectories(storagePath.getParent());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, storagePath, StandardCopyOption.REPLACE_EXISTING);
            }
            Document document = new Document();
            document.setPatient(patient);
            document.setVisit(visit);
            document.setFileName(originalName);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setStoragePath(storagePath.toString());
            document.setChecksum(calculateChecksum(storagePath));
            documentRepository.save(document);
            return documentMapper.toResponse(document);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to store file", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public InputStream download(final Long documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        try {
            return Files.newInputStream(Path.of(document.getStoragePath()));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read file", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> listByPatient(final Long patientId) {
        return documentRepository.findByPatientId(patientId).stream()
            .map(documentMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> listByVisit(final Long visitId) {
        return documentRepository.findByVisitId(visitId).stream()
            .map(documentMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        documentRepository.delete(document);
        try {
            Files.deleteIfExists(Path.of(document.getStoragePath()));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to delete file", e);
        }
    }

    private Path resolveStoragePath(final Long patientId, final Long visitId, final String originalName) {
        String sanitized = originalName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        if (visitId != null) {
            return rootDirectory.resolve("patients")
                .resolve(patientId.toString())
                .resolve("visits")
                .resolve(visitId.toString())
                .resolve(sanitized);
        }
        return rootDirectory.resolve("patients")
            .resolve(patientId.toString())
            .resolve("documents")
            .resolve(sanitized);
    }

    private String calculateChecksum(final Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = Files.readAllBytes(path);
            return HexFormat.of().formatHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new IllegalStateException("Unable to calculate checksum", e);
        }
    }
}










