package com.hyeji.petbook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    private String keyword;
    private Integer page;
    private Integer size;
}
