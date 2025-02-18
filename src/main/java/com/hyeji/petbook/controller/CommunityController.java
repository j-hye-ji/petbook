package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestHeader(name = "Authorization") String token,
                                             @RequestBody PostDTO postDTO) {
        String message = communityService.createPost(token, postDTO);
        return ResponseEntity.ok(message);
    }

    // 모든 게시글 조회
    @GetMapping("/")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> allPosts = communityService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePost(@PathVariable(name = "id") Long id, @RequestBody PostDTO postDTO) {
        Post post = communityService.updatePost(id, postDTO);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        String responseMessage = String.format("게시글 제목: %s\n", post.getTitle()) +
                String.format("게시글 내용: %s", post.getContents());

        return ResponseEntity.ok(responseMessage);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
        boolean post = communityService.deletePost(id);

        if (!post) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글이 존재하지 않거나 삭제할 수 없습니다.");
        }

        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
