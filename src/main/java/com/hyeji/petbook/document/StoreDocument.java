package com.hyeji.petbook.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "store_index")  // Elasticsearch 인덱스명
public class StoreDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard") // 한글 검색 지원
    private String storeName;

    @Field(type = FieldType.Text)
    private String location;

    @Field(type = FieldType.Text)
    private String description;
}
