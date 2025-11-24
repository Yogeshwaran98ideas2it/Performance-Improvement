-- Create default admin user
-- Password: SecureAdmin@2025! (BCrypt hash - CHANGE THIS IN PRODUCTION)
-- This migration creates an initial admin user for system access
-- IMPORTANT: Change this password immediately after first login in production

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
    '$2a$10$BBS71Lx/0z2vo/pAedi0XuwwgixEAid5OL7xQs5DzO5SSJJZ1ywS.', -- SecureAdmin@2025! (BCrypt hash)
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








