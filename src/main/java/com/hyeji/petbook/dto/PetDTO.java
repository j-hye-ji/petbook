package com.hyeji.petbook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetDTO {
    @NotBlank
    private String petName;
    @NotBlank
    private String type;
    private String breed;
    private LocalDate birthDate;
    private String gender;
    private String healthStatus;
}
