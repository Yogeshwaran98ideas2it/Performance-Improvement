-- Initial database schema for Healthcare Management System
-- This migration creates all core tables with proper relationships and constraints

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

-- Role Permissions table
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_name VARCHAR(120) NOT NULL,
    resource VARCHAR(120) NOT NULL,
    action VARCHAR(40) NOT NULL,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_name)
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(20),
    address TEXT,
    city VARCHAR(80),
    state VARCHAR(80),
    zip_code VARCHAR(20),
    country VARCHAR(80),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_user_username_email UNIQUE (username, email)
);

-- Users-Roles join table
CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    mrn VARCHAR(80) NOT NULL UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(120),
    address TEXT,
    city VARCHAR(80),
    state VARCHAR(80),
    zip_code VARCHAR(20),
    country VARCHAR(80),
    emergency_contact_name VARCHAR(160),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(80),
    blood_type VARCHAR(10),
    height_cm DECIMAL(5,2),
    weight_kg DECIMAL(5,2),
    vital_signs JSONB,
    past_medical_history TEXT,
    primary_physician_id BIGINT,
    secondary_physician_id BIGINT,
    referral_physician_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_patient_primary_physician FOREIGN KEY (primary_physician_id) REFERENCES users(id),
    CONSTRAINT fk_patient_secondary_physician FOREIGN KEY (secondary_physician_id) REFERENCES users(id),
    CONSTRAINT fk_patient_referral_physician FOREIGN KEY (referral_physician_id) REFERENCES users(id)
);

-- User Patient Assignments table
CREATE TABLE IF NOT EXISTS user_patient_assignments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    assignment_role VARCHAR(80),
    start_date DATE,
    end_date DATE,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_assignment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Patient Visits table
CREATE TABLE IF NOT EXISTS patient_visits (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    physician_id BIGINT,
    visit_time TIMESTAMP NOT NULL,
    visit_type VARCHAR(80),
    location VARCHAR(120),
    reason VARCHAR(255),
    notes TEXT,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_visit_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_visit_physician FOREIGN KEY (physician_id) REFERENCES users(id)
);

-- Visit Patient Diagnoses table
CREATE TABLE IF NOT EXISTS visit_patient_diagnoses (
    id BIGSERIAL PRIMARY KEY,
    visit_id BIGINT NOT NULL,
    code VARCHAR(40) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(40),
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_diagnosis_visit FOREIGN KEY (visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE
);

-- Visit Patient Allergies table
CREATE TABLE IF NOT EXISTS visit_patient_allergies (
    id BIGSERIAL PRIMARY KEY,
    visit_id BIGINT NOT NULL,
    allergen VARCHAR(120) NOT NULL,
    reaction VARCHAR(255),
    severity VARCHAR(40),
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_allergy_visit FOREIGN KEY (visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE
);

-- Visit Patient Medications table
CREATE TABLE IF NOT EXISTS visit_patient_medications (
    id BIGSERIAL PRIMARY KEY,
    visit_id BIGINT NOT NULL,
    medication_name VARCHAR(160) NOT NULL,
    dosage VARCHAR(80),
    frequency VARCHAR(80),
    route VARCHAR(40),
    instructions VARCHAR(255),
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_medication_visit FOREIGN KEY (visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE
);

-- Documents table
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT,
    visit_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(80) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(120),
    description TEXT,
    uploaded_by BIGINT,
    created_by VARCHAR(80),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(80),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_document_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_visit FOREIGN KEY (visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_uploader FOREIGN KEY (uploaded_by) REFERENCES users(id),
    CONSTRAINT chk_document_reference CHECK (patient_id IS NOT NULL OR visit_id IS NOT NULL)
);

-- Refresh Tokens table
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_patients_mrn ON patients(mrn);
CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(last_name, first_name);
CREATE INDEX IF NOT EXISTS idx_assignments_user ON user_patient_assignments(user_id);
CREATE INDEX IF NOT EXISTS idx_assignments_patient ON user_patient_assignments(patient_id);
CREATE INDEX IF NOT EXISTS idx_visits_patient ON patient_visits(patient_id);
CREATE INDEX IF NOT EXISTS idx_visits_physician ON patient_visits(physician_id);
CREATE INDEX IF NOT EXISTS idx_visits_time ON patient_visits(visit_time);
CREATE INDEX IF NOT EXISTS idx_diagnoses_visit ON visit_patient_diagnoses(visit_id);
CREATE INDEX IF NOT EXISTS idx_allergies_visit ON visit_patient_allergies(visit_id);
CREATE INDEX IF NOT EXISTS idx_medications_visit ON visit_patient_medications(visit_id);
CREATE INDEX IF NOT EXISTS idx_documents_patient ON documents(patient_id);
CREATE INDEX IF NOT EXISTS idx_documents_visit ON documents(visit_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expiry ON refresh_tokens(expiry_date);










