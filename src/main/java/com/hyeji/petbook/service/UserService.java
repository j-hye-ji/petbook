package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
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

    // 로그인
    public String logIn(UserDTO userDTO) {
        Optional<User> optUser = Optional.ofNullable(userRepository.findByEmail(userDTO.getEmail()));
        if (!optUser.isPresent()) {
            return null;
        }

        User user = optUser.get();

        // 비밀번호는 암호화된 값과 비교해야 함 (예: BCrypt, PBKDF2 등)
        if (user.getEmail().equals(userDTO.getEmail()) && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            // 로그인 성공시 JWT 토큰 생성 및 반환
            return jwtTokenUtil.generateToken(user);  // JWT 토큰 반환
        }

        return "로그인 실패";  // 실패 시 메시지 반환
    }
}
