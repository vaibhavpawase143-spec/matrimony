CREATE TABLE states (
                        id BIGSERIAL PRIMARY KEY,
                        admin_id BIGINT,
                        name VARCHAR(120) NOT NULL,
                        country_id BIGINT NOT NULL,
                        status BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NULL,
                        CONSTRAINT fk_state_admin
