CREATE USER audit_app WITH PASSWORD 'change-me';
CREATE DATABASE audit OWNER audit_app;
GRANT ALL PRIVILEGES ON DATABASE audit TO audit_app;

-- Ensure healthcare_app can access audit database if needed
GRANT CONNECT ON DATABASE audit TO healthcare_app;










