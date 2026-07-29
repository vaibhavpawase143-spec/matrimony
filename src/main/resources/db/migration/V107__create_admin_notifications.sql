CREATE TABLE admin_notifications (

    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    title VARCHAR(255) NOT NULL,

    message TEXT NOT NULL,

    type VARCHAR(30) NOT NULL,

    read BOOLEAN NOT NULL DEFAULT FALSE,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_admin_notification_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_admin_notification_type
        CHECK (
            type IN (
                'REPORT',
                'SUPPORT',
                'NEW_USER',
                'SUBSCRIPTION',
                'ADMIN',
                'SYSTEM',
                'ANNOUNCEMENT',
                'WARNING',
                'MAINTENANCE'
            )
        )
);

CREATE INDEX idx_admin_notification_admin
ON admin_notifications(admin_id);

CREATE INDEX idx_admin_notification_read
ON admin_notifications(admin_id, read);

CREATE INDEX idx_admin_notification_created
ON admin_notifications(admin_id, created_at);