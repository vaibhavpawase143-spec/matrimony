CREATE TABLE profiles (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT UNIQUE,

religion_id BIGINT,

caste_id BIGINT,

education_level_id BIGINT,

occupation_id BIGINT,

height_id BIGINT,

weight_id BIGINT,

city_id BIGINT,

about TEXT,

CONSTRAINT fk_profile_user
    FOREIGN KEY (user_id)
    REFERENCES users(id),

CONSTRAINT fk_profile_religion
    FOREIGN KEY (religion_id)
    REFERENCES religions(id),

CONSTRAINT fk_profile_caste
    FOREIGN KEY (caste_id)
    REFERENCES castes(id),

CONSTRAINT fk_profile_education_level
    FOREIGN KEY (education_level_id)
    REFERENCES education_levels(id),

CONSTRAINT fk_profile_occupation
    FOREIGN KEY (occupation_id)
    REFERENCES occupations(id),

CONSTRAINT fk_profile_height
    FOREIGN KEY (height_id)
    REFERENCES heights(id),

CONSTRAINT fk_profile_weight
    FOREIGN KEY (weight_id)
    REFERENCES weights(id),

CONSTRAINT fk_profile_city
    FOREIGN KEY (city_id)
    REFERENCES cities(id)
```

);
