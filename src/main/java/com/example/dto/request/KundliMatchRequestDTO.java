package com.example.dto.request;

import com.example.model.BirthDetails;
import lombok.Data;

@Data
public class KundliMatchRequestDTO {
    private BirthDetails male;
    private BirthDetails female;
}