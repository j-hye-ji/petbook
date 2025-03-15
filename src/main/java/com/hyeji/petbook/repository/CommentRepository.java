package com.hyeji.petbook.repository;

import com.hyeji.petbook.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllById(Long postId);

    /* 게시글의 댓글 리스트 조회 (Join Fetch 사용 JPQL 추가) */
    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);
}
