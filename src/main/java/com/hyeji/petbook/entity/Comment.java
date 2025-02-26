package com.hyeji.petbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post; // 어떤 게시글에 달린 댓글인지

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // 댓글 작성자

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
