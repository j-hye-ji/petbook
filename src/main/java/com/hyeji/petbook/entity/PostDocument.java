package com.hyeji.petbook.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_index")
public class PostDocument {

    @Id
    private String id;

    private String title;
    private String contents;
}
