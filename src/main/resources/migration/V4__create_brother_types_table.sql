CREATE TABLE brother_types (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

value VARCHAR(100) NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_brothertype_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);

-- Index
CREATE INDEX idx_brother_type_value ON brother_types(value);
