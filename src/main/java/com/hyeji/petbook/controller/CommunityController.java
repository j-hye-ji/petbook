package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.service.CommunityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
}
