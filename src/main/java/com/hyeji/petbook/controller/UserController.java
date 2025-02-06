package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.service.UserService;
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
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        String message = userService.signUp(userDTO);
        return ResponseEntity.ok(message);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String result = userService.logIn(userDTO);  // 로그인 결과 (로그인 성공/실패 메시지 또는 토큰)

        if (result.startsWith("로그인 실패")) {
            // 로그인 실패 시 Unauthorized 응답을 보낸다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(result);  // 로그인 실패 메시지를 응답으로 반환
        }

        // 로그인 성공 시 JWT 토큰을 반환
        return ResponseEntity.ok(result);  // 성공적으로 토큰 반환
    }

    // 모든 회원 조회
    @GetMapping("/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
