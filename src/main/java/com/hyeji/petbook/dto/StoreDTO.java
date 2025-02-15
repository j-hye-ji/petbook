package com.hyeji.petbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StoreDTO {
    @NotBlank(message = "매장 이름을 입력하세요.")
    private String storeName;

    @NotBlank(message = "위치 정보를 입력하세요.")
    private String location;

    @NotBlank(message = "매장 설명을 입력하세요.")
    private String description;

    @NotBlank(message = "매장 오픈 시간을 입력하세요.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @NotBlank(message = "매장 마감 시간을 입력하세요.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
}
