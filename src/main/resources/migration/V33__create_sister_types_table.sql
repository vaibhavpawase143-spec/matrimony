CREATE TABLE sister_types (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

value VARCHAR(100) NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_sister_type_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);

-- Index
CREATE INDEX idx_sister_type_value ON sister_types(value);
