package com.acme.healthcare.mapper;

import com.acme.healthcare.domain.entity.Document;
import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.service.dto.DocumentResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DocumentMapper}.
 */
class DocumentMapperTest {

    private final DocumentMapper mapper = new DocumentMapper();

    @Test
    void toResponse_ShouldMapPatientAndVisitIds() {
        Patient patient = new Patient();
        patient.setId(5L);
        PatientVisit visit = new PatientVisit();
        visit.setId(9L);

        Document document = new Document();
        document.setId(1L);
        document.setPatient(patient);
        document.setVisit(visit);
        document.setFileName("report.pdf");
        document.setFileType("application/pdf");
        document.setFileSize(2048L);

        DocumentResponse response = mapper.toResponse(document);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPatientId()).isEqualTo(5L);
        assertThat(response.getVisitId()).isEqualTo(9L);
        assertThat(response.getFileName()).isEqualTo("report.pdf");
        assertThat(response.getFileType()).isEqualTo("application/pdf");
        assertThat(response.getFileSize()).isEqualTo(2048L);
    }
}


