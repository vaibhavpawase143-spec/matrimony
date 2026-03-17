CREATE TABLE religions (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(120) NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_religion_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);

-- Index
CREATE INDEX idx_religion_name ON religions(name);
