-- Insert default roles and permissions
-- This migration seeds the database with initial role and permission data

-- Insert default roles
INSERT INTO roles (name, description, created_by, created_at, updated_by, updated_at, version) VALUES
('ADMIN', 'System Administrator with full access', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
('PHYSICIAN', 'Medical doctor with patient care responsibilities', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
('NURSE', 'Nursing staff with patient care support', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0),
('THERAPIST', 'Therapy provider with specialized care', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0)
ON CONFLICT (name) DO NOTHING;

-- Insert default permissions for ADMIN role
INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 'USER_CREATE', 'USER', 'CREATE', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r WHERE r.name = 'ADMIN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 'USER_READ', 'USER', 'READ', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r WHERE r.name = 'ADMIN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 'USER_UPDATE', 'USER', 'UPDATE', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r WHERE r.name = 'ADMIN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 'USER_DELETE', 'USER', 'DELETE', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r WHERE r.name = 'ADMIN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

-- Insert permissions for all resources for ADMIN (comprehensive permissions)
INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 
    CASE 
        WHEN res.resource = 'PATIENT' THEN 'PATIENT_' || act.action
        WHEN res.resource = 'VISIT' THEN 'VISIT_' || act.action
        WHEN res.resource = 'DIAGNOSIS' THEN 'DIAGNOSIS_' || act.action
        WHEN res.resource = 'ALLERGY' THEN 'ALLERGY_' || act.action
        WHEN res.resource = 'MEDICATION' THEN 'MEDICATION_' || act.action
        WHEN res.resource = 'ASSIGNMENT' THEN 'ASSIGNMENT_' || act.action
        WHEN res.resource = 'DOCUMENT' THEN 'DOCUMENT_' || act.action
        WHEN res.resource = 'ROLE' THEN 'ROLE_' || act.action
        WHEN res.resource = 'AUDIT' THEN 'AUDIT_' || act.action
    END as permission_name,
    res.resource,
    act.action,
    'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r
CROSS JOIN (VALUES ('PATIENT'), ('VISIT'), ('DIAGNOSIS'), ('ALLERGY'), ('MEDICATION'), ('ASSIGNMENT'), ('DOCUMENT'), ('ROLE'), ('AUDIT')) AS res(resource)
CROSS JOIN (VALUES ('CREATE'), ('READ'), ('UPDATE'), ('DELETE')) AS act(action)
WHERE r.name = 'ADMIN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

-- Insert permissions for PHYSICIAN role
INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 
    CASE 
        WHEN res.resource = 'PATIENT' THEN 'PATIENT_' || act.action
        WHEN res.resource = 'VISIT' THEN 'VISIT_' || act.action
        WHEN res.resource = 'DIAGNOSIS' THEN 'DIAGNOSIS_' || act.action
        WHEN res.resource = 'ALLERGY' THEN 'ALLERGY_' || act.action
        WHEN res.resource = 'MEDICATION' THEN 'MEDICATION_' || act.action
        WHEN res.resource = 'DOCUMENT' THEN 'DOCUMENT_' || act.action
    END as permission_name,
    res.resource,
    act.action,
    'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r
CROSS JOIN (VALUES ('PATIENT'), ('VISIT'), ('DIAGNOSIS'), ('ALLERGY'), ('MEDICATION'), ('DOCUMENT')) AS res(resource)
CROSS JOIN (VALUES ('CREATE'), ('READ'), ('UPDATE'), ('DELETE')) AS act(action)
WHERE r.name = 'PHYSICIAN'
ON CONFLICT (role_id, permission_name) DO NOTHING;

-- Insert permissions for NURSE role (read and limited create/update)
INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 
    CASE 
        WHEN res.resource = 'PATIENT' THEN 'PATIENT_READ'
        WHEN res.resource = 'VISIT' THEN 'VISIT_' || act.action
        WHEN res.resource = 'ALLERGY' THEN 'ALLERGY_' || act.action
        WHEN res.resource = 'DOCUMENT' THEN 'DOCUMENT_' || act.action
    END as permission_name,
    res.resource,
    act.action,
    'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r
CROSS JOIN (VALUES ('PATIENT'), ('VISIT'), ('ALLERGY'), ('DOCUMENT')) AS res(resource)
CROSS JOIN (VALUES ('CREATE'), ('READ'), ('UPDATE'), ('DELETE')) AS act(action)
WHERE r.name = 'NURSE'
  AND (res.resource != 'PATIENT' OR act.action = 'READ')
ON CONFLICT (role_id, permission_name) DO NOTHING;

-- Insert permissions for THERAPIST role
INSERT INTO role_permissions (role_id, permission_name, resource, action, created_by, created_at, updated_by, updated_at, version)
SELECT r.id, 
    CASE 
        WHEN res.resource = 'PATIENT' THEN 'PATIENT_READ'
        WHEN res.resource = 'VISIT' THEN 'VISIT_' || act.action
        WHEN res.resource = 'DOCUMENT' THEN 'DOCUMENT_' || act.action
    END as permission_name,
    res.resource,
    act.action,
    'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 0
FROM roles r
CROSS JOIN (VALUES ('PATIENT'), ('VISIT'), ('DOCUMENT')) AS res(resource)
CROSS JOIN (VALUES ('CREATE'), ('READ'), ('UPDATE'), ('DELETE')) AS act(action)
WHERE r.name = 'THERAPIST'
  AND (res.resource != 'PATIENT' OR act.action = 'READ')
ON CONFLICT (role_id, permission_name) DO NOTHING;


