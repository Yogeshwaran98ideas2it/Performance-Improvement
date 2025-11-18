package com.acme.healthcare.controller;

import com.acme.healthcare.service.DocumentService;
import com.acme.healthcare.service.dto.DocumentResponse;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * DocumentController manages document upload and retrieval endpoints.
 */
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Creates the controller.
     *
     * @param documentService the document service
     */
    public DocumentController(final DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Uploads a document and associates it with patient and optionally visit.
     *
     * @param patientId the patient identifier
     * @param visitId the visit identifier
     * @param file the multipart file
     * @return the document response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGE')")
    public ResponseEntity<DocumentResponse> upload(@RequestParam final Long patientId,
                                                   @RequestParam(required = false) final Long visitId,
                                                   @RequestParam final MultipartFile file) {
        return ResponseEntity.ok(documentService.upload(patientId, visitId, file));
    }

    /**
     * Downloads a document.
     *
     * @param documentId the document identifier
     * @return the document stream
     */
    @GetMapping("/{documentId}")
    @PreAuthorize("hasAuthority('DOCUMENT_READ')")
    public ResponseEntity<InputStreamResource> download(@PathVariable final Long documentId) {
        InputStream stream = documentService.download(documentId);
        InputStreamResource resource = new InputStreamResource(stream);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document_" + documentId + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    /**
     * Lists patient documents.
     *
     * @param patientId the patient identifier
     * @return the document list
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('DOCUMENT_READ')")
    public ResponseEntity<List<DocumentResponse>> listByPatient(@PathVariable final Long patientId) {
        return ResponseEntity.ok(documentService.listByPatient(patientId));
    }

    /**
     * Lists visit documents.
     *
     * @param visitId the visit identifier
     * @return the document list
     */
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('DOCUMENT_READ')")
    public ResponseEntity<List<DocumentResponse>> listByVisit(@PathVariable final Long visitId) {
        return ResponseEntity.ok(documentService.listByVisit(visitId));
    }

    /**
     * Deletes a document.
     *
     * @param documentId the document identifier
     * @return the response
     */
    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable final Long documentId) {
        documentService.delete(documentId);
        return ResponseEntity.noContent().build();
    }
}


