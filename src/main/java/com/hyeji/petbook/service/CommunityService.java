package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.CommentDTO;
import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Comment;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.CommentRepository;
import com.hyeji.petbook.repository.PostRepository;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private static final String POST_LIKE_COUNT_KEY = "post:%d:likes";  // 게시글 좋아요 수 저장
    private static final String USER_LIKE_KEY = "user:%d:liked:%d";     // 사용자가 게시글에 좋아요를 눌렀는지 여부

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // 게시글 작성
    public String createPost(String token, PostDTO postDTO) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(postDTO.getTitle());
        post.setContents(postDTO.getContents());

        postRepository.save(post);

        return "작성하신 글이 등록되었습니다.";
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 수정
    public Post updatePost(Long id, PostDTO postDTO) {
        Optional<Post> optPost = postRepository.findById(id);
        if (!optPost.isPresent()) {
            return null;
        }

        Post updatePost = optPost.get();

        if (updatePost.getTitle() != null) {
            updatePost.setTitle(postDTO.getTitle());
        }

        if (updatePost.getContents() != null) {
            updatePost.setContents(postDTO.getContents());
        }

        return postRepository.save(updatePost);
    }

    // 게시글 삭제
    public boolean deletePost(Long id) {
        Optional<Post> optPost = postRepository.findById(id);
        if (!optPost.isPresent()) {
            return false;
        }

        postRepository.delete(optPost.get());

        return true;
    }

    // 게시글 좋아요
    public void addLike(String token, Long postId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        // Redis에서 해당 사용자가 이미 좋아요를 눌렀는지 확인
        String userLikeKey = String.format(USER_LIKE_KEY, userId, postId);
        Boolean alreadyLiked = redisTemplate.hasKey(userLikeKey);

        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        // 게시글 좋아요 추가
        redisTemplate.opsForValue().increment(String.format(POST_LIKE_COUNT_KEY, postId), 1);

        // 사용자가 좋아요를 눌렀다는 정보를 Redis에 저장
        redisTemplate.opsForValue().set(userLikeKey, String.valueOf(true));

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        post.incrementLikesCount();
        postRepository.save(post);
    }

    // 해당 게시글의 좋아요 개수 조회
    public long getLikesByPost(Long id) {
        String likeCountKey = String.format(POST_LIKE_COUNT_KEY, id);

        /* 레디스는 무조건 문자열로 저장이 됨. -> 데이터를 꺼낼 때 일시적 다운캐스팅으로 타입을 지정 */
        String likeCountString = (String) redisTemplate.opsForValue().get(likeCountKey);
        long likeCount = likeCountString != null ? Long.parseLong(likeCountString) : 0L;

        // 레디스에 값이 없으면 DB에서 조회
        if (likeCount == 0L) {
            Optional<Post> optPost = postRepository.findById(id);
            return optPost.map(Post::getLikesCount).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        }

        return likeCount;
    }

    // 게시글 좋아요 취소
    public boolean deleteLike(String token, Long postId) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        String userLikeKey = String.format(USER_LIKE_KEY, userId, postId);
        Boolean alreadyLiked = redisTemplate.hasKey(userLikeKey);

        if (!alreadyLiked) {
            return false;
        }

        // 게시글 좋아요 수 감소 (Redis)
        redisTemplate.opsForValue().decrement(String.format(POST_LIKE_COUNT_KEY, postId), 1);

        /* 해당 사용자가 게시글에 대해 좋아요를 누른 기록이 Redis에서 제거 */
        redisTemplate.delete(userLikeKey);

        // 게시글 좋아요 수 감소 (DB)
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        post.decrementLikesCount();
        postRepository.save(post);

        return true;
    }

    // 댓글 작성
    public String createComment(String token, CommentDTO commentDTO) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());

        commentRepository.save(comment);

        return "댓글이 작성되었습니다.";
    }

    // 댓글 수정
    public Comment updateComment(String token, Long commentId, CommentDTO commentDTO) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 댓글 작성자와 동일한지 확인
        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("댓글을 수정할 수 없습니다.");
        }

        comment.setContent(commentDTO.getContent());

        commentRepository.save(comment);

        return comment;
    }

    // 댓글 조회
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findAllById(postId);
    }

    /* 게시글의 댓글 리스트 조회 (Join Fetch 사용 JPQL 추가) */
    public List<Comment> getCommentsByPostJPQL(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 삭제
    public boolean deleteComment(String token, Long postId, Long commentId) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 해당 게시글의 댓글이 맞는지 확인
        if (!comment.getPost().getId().equals(postId)) {
            return false;
        }

        // 댓글 작성자가 동일한지 확인
        if (!comment.getUser().getEmail().equals(email)) {
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }
}
