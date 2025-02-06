package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.UserDTO;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.type.UserRole;
import com.hyeji.petbook.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    // 회원가입
    public String signUp(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // userRole을 클라이언트가 보내지 않으면 USER로 기본 설정
        UserRole userRole = userDTO.getUserRole() != null ? userDTO.getUserRole() : UserRole.USER;

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(encodedPassword);
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserRole(userRole);  // 역할 설정

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
