CREATE TABLE states (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(120) NOT NULL,

country_id BIGINT NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_state_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id),

CONSTRAINT fk_state_country
    FOREIGN KEY (country_id)
    REFERENCES countries(id)
```

);

-- Indexes
CREATE INDEX idx_state_name ON states(name);
CREATE INDEX idx_state_country ON states(country_id);
