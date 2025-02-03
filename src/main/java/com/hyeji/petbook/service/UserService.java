package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // 회원가입
    public String signUp(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(encodedPassword);
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(user);

        return "회원가입 성공";
    }
}
