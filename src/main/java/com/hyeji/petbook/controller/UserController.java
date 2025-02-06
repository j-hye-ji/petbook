package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDTO userDTO) {
        String message = userService.signUp(userDTO);
        return ResponseEntity.ok(message);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String token = userService.logIn(userDTO);  // 로그인 결과로 토큰을 받는다.

        if (token == null) {
            // 로그인 실패 시 Unauthorized 응답을 보낸다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 로그인 성공 시 JWT 토큰을 반환
        return ResponseEntity.ok(token);  // 성공적으로 토큰 반환
    }

    // 모든 회원 조회
    @GetMapping("/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
