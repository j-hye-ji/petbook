package com.hyeji.petbook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetDTO {
    @NotBlank(message = "반려동물 이름은 필수 입력 항목입니다.")
    private String petName;
    @NotBlank(message = "반려동물의 종류는 필수 입력 항목입니다.")
    private String type;
    private String breed;
    private LocalDate birthday;
    private String gender;
    private String healthStatus;
}
