package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.CommentDTO;
import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Comment;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "Authorization")
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // 게시글 작성
    @PostMapping("/create-post")
    public ResponseEntity<String> createPost(@RequestHeader(name = "Authorization") String token,
                                             @RequestBody PostDTO postDTO) {
        String message = communityService.createPost(token, postDTO);
        return ResponseEntity.ok(message);
    }

    // 모든 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> allPosts = communityService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    // 게시글 수정
    @PutMapping("/update-post/{id}")
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
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
        boolean post = communityService.deletePost(id);

        if (!post) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글이 존재하지 않거나 삭제할 수 없습니다.");
        }

        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 게시글 좋아요
    @PostMapping("/add-like/{id}")
    public ResponseEntity<String> addLike(@RequestHeader(name = "Authorization") String token,
                                          @PathVariable(name = "id") Long postId) {
        communityService.addLike(token, postId);
        return ResponseEntity.ok("좋아요가 추가되었습니다.");
    }

    // 해당 게시글의 좋아요 개수 조회
    @GetMapping("/likes/{id}")
    public ResponseEntity<Long> getLikesByPost(@PathVariable(name = "id") Long id) {
        Long likes = communityService.getLikesByPost(id);
        return ResponseEntity.ok(likes);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/delete-like/{id}")
    public ResponseEntity<String> deleteLike(@RequestHeader(name = "Authorization") String token,
                                             @PathVariable(name = "id") Long postId) {
        boolean result = communityService.deleteLike(token, postId);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("좋아요를 누르지 않은 게시글입니다.");
        }

        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }

    // 댓글 작성
    @PostMapping("/create-comment")
    public ResponseEntity<String> createComment(@RequestHeader(name = "Authorization") String token,
                                                @RequestBody CommentDTO commentDTO) {
        String message = communityService.createComment(token, commentDTO);
        return ResponseEntity.ok(message);
    }

    // 댓글 수정
    @PutMapping("/update-comment/{id}")
    public ResponseEntity<String> updateComment(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable(name = "id") Long commentId,
                                                @RequestBody CommentDTO commentDTO) {
        Comment comment = communityService.updateComment(token, commentId, commentDTO);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        String responseMessage = String.format("댓글 수정: %s", comment.getContent());

        return ResponseEntity.ok(responseMessage);
    }

    // 댓글 조회
    @GetMapping("/comments/{id}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable(name = "id") Long postId) {
        List<Comment> comments = communityService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<Comment>> getCommentsByPostJPQL(@PathVariable(name = "id") Long postId) {
        List<Comment> comments = communityService.getCommentsByPostJPQL(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<String> deleteComment(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable(name = "id") Long postId,
                                                @RequestBody Long commentId) {
        boolean result = communityService.deleteComment(token, postId, commentId);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 게시글의 댓글이 아니거나 댓글 삭제 권한이 없습니다.");
        }

        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
