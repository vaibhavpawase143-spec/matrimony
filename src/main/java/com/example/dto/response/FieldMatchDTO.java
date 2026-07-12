package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldMatchDTO {

    // Example : Religion, Caste, Height
    private String fieldName;

    // Logged In User Value
    private String myValue;

    // Partner Value
    private String partnerValue;

    // true = Match
    // false = Not Match
    private boolean matched;

}