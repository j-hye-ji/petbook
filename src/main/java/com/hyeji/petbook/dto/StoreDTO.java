package com.hyeji.petbook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {
    @NotBlank(message = "매장 이름을 입력하세요.")
    private String storeName;

    @NotBlank(message = "위치 정보를 입력하세요.")
    private String location;

    @NotBlank(message = "매장 설명을 입력하세요.")
    private String description;
}
