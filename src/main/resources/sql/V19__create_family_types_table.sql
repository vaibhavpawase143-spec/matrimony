CREATE TABLE family_types (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(100) NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_family_type_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);

-- Index
CREATE INDEX idx_family_type_name ON family_types(name);
