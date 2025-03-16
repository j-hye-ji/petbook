package com.hyeji.petbook.repository;

import com.hyeji.petbook.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p where p.likesCount >= 10 ORDER BY p.likesCount DESC")
    List<Post> findPopularPosts();
}
