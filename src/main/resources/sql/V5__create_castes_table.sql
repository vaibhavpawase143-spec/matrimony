CREATE TABLE castes (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

admin_id BIGINT,

name VARCHAR(150) NOT NULL,

religion_id BIGINT,

status BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_caste_admin
    FOREIGN KEY (admin_id)
    REFERENCES admins(id),

CONSTRAINT fk_caste_religion
    FOREIGN KEY (religion_id)
    REFERENCES religions(id)
```

);
