package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.entity.User;
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
}
