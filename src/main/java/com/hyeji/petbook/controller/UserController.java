package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        String message = userService.signUp(userDTO);
        return ResponseEntity.ok(message);
    }

    // 모든 회원 조회
    @GetMapping("/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
