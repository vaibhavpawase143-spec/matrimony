CREATE TABLE family_details (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

profile_id BIGINT UNIQUE,

family_type_id BIGINT,

family_status_id BIGINT,

brother_type_id BIGINT,

sister_type_id BIGINT,

father_occupation VARCHAR(150),

mother_occupation VARCHAR(150),

CONSTRAINT fk_family_profile
    FOREIGN KEY (profile_id)
    REFERENCES profiles(id),

CONSTRAINT fk_family_type
    FOREIGN KEY (family_type_id)
    REFERENCES family_types(id),

CONSTRAINT fk_family_status
    FOREIGN KEY (family_status_id)
    REFERENCES family_status(id),

CONSTRAINT fk_family_brother
    FOREIGN KEY (brother_type_id)
    REFERENCES brother_types(id),

CONSTRAINT fk_family_sister
    FOREIGN KEY (sister_type_id)
    REFERENCES sister_types(id)
```

);
