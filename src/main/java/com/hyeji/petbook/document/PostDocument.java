package com.hyeji.petbook.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_index")
public class PostDocument {

    @Id
    private String id;

    @Field(type = Text)
    private String title;

    @Field(type = Text)
    private String contents;
}
