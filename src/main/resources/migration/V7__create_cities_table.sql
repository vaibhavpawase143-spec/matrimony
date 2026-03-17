CREATE TABLE cities (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(120) NOT NULL,

state_id BIGINT NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_city_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id),

CONSTRAINT fk_city_state
    FOREIGN KEY (state_id)
    REFERENCES states(id)
```

);

-- Indexes
CREATE INDEX idx_city_name ON cities(name);
CREATE INDEX idx_city_state ON cities(state_id);
