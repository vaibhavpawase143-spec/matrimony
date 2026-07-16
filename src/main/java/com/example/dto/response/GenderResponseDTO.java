package com.example.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenderResponseDTO {

    private Long id;
    private String name;
}