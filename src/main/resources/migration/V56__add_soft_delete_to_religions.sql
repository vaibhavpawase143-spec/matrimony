-- V56__add_soft_delete_to_religions.sql
-- Add soft delete support to religions table

ALTER TABLE religions
ADD COLUMN deleted_at TIMESTAMP NULL,
ADD COLUMN deleted_by BIGINT NULL;

-- Create indexes for soft delete queries
CREATE INDEX idx_religion_deleted_at ON religions(deleted_at);
CREATE INDEX idx_religion_deleted_by ON religions(deleted_by);
CREATE INDEX idx_religion_is_active ON religions(is_active);

-- Add foreign key constraint for deleted_by
ALTER TABLE religions
ADD CONSTRAINT fk_religions_deleted_by
    FOREIGN KEY (deleted_by)
    REFERENCES admins(id)
    ON DELETE SET NULL;

