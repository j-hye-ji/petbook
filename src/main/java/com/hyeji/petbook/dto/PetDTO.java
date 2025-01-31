package com.hyeji.petbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetDTO {
    private String petName;
    private String type;
    private String breed;
    private LocalDate birthDate;
    private String gender;
    private String healthStatus;
}
