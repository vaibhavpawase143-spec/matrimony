CREATE TABLE fields_of_study (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(150) NOT NULL,

status BOOLEAN NOT NULL DEFAULT TRUE,

created_at TIMESTAMP NULL,

updated_at TIMESTAMP NULL,

CONSTRAINT fk_field_of_study_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id)
```

);

-- Index
CREATE INDEX idx_field_of_study_name ON fields_of_study(name);
