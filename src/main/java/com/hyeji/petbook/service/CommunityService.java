package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.PostDTO;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.CommentRepository;
import com.hyeji.petbook.repository.PostRepository;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

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
}
