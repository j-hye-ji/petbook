package com.hyeji.petbook.document;

import com.hyeji.petbook.entity.Post;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_index")
@Builder
public class PostDocument {

    @Id
    private String id;
    private String title;
    private String contents;
    private LocalDateTime createAt;

    public static PostDocument from(Post post) {
        return PostDocument.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .contents(post.getContents())
                .createAt(post.getCreatedAt())
                .build();
    }
}
