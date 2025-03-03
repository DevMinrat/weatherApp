-- add session_id column to sessions table

ALTER TABLE sessions
    ADD COLUMN session_id VARCHAR(36) UNIQUE NOT NULL;

-- generate indexes
CREATE UNIQUE INDEX idx_sessions_id ON sessions (session_id);
CREATE INDEX idx_sessions_expires_at ON sessions (expiresat);