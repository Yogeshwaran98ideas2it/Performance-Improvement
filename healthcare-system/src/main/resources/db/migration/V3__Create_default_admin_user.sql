-- Create default admin user
-- Password: admin123 (BCrypt hash - should be changed in production)
-- This migration creates an initial admin user for system access

INSERT INTO users (
    username, 
    password_hash, 
    email, 
    first_name, 
    last_name, 
    is_active, 
    created_by, 
    created_at, 
    updated_by, 
    updated_at, 
    version
) VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwy8pQ5O', -- admin123
    'admin@healthcare.com',
    'System',
    'Administrator',
    TRUE,
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP,
    0
) ON CONFLICT (username) DO NOTHING;

-- Assign ADMIN role to the default admin user
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;


