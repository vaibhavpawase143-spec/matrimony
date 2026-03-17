CREATE TABLE partner_preferences (

```
id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT UNIQUE,

min_age INT,
max_age INT,

religion_id BIGINT,
caste_id BIGINT,
city_id BIGINT,

CONSTRAINT fk_partner_pref_user
    FOREIGN KEY (user_id)
    REFERENCES users(id),

CONSTRAINT fk_partner_pref_religion
    FOREIGN KEY (religion_id)
    REFERENCES religions(id),

CONSTRAINT fk_partner_pref_caste
    FOREIGN KEY (caste_id)
    REFERENCES castes(id),

CONSTRAINT fk_partner_pref_city
    FOREIGN KEY (city_id)
    REFERENCES cities(id)
```

);
