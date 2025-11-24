-- Fix refresh_tokens table to match RefreshToken entity
-- The entity extends AuditableEntity and uses different column names

-- Add audit columns if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'created_by') THEN
        ALTER TABLE refresh_tokens ADD COLUMN created_by VARCHAR(80);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'updated_by') THEN
        ALTER TABLE refresh_tokens ADD COLUMN updated_by VARCHAR(80);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'updated_at') THEN
        ALTER TABLE refresh_tokens ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'version') THEN
        ALTER TABLE refresh_tokens ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
    END IF;
END $$;

-- Rename columns to match entity field names (only if they exist and haven't been renamed)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'expiry_date') THEN
        ALTER TABLE refresh_tokens RENAME COLUMN expiry_date TO expires_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'refresh_tokens' AND column_name = 'is_revoked') THEN
        ALTER TABLE refresh_tokens RENAME COLUMN is_revoked TO revoked;
    END IF;
END $$;

