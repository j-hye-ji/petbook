package com.hyeji.petbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 사용자 정보를 메모리에 저장하는 UserDetailsService 설정
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("password"))  // 암호화된 비밀번호 사용
                .roles("USER")
                .build());
        return manager;
    }

    // PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt로 암호화
    }

    // SecurityFilterChain 사용하여 보안 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // REST API에서는 CSRF 보호를 비활성화
                .authorizeRequests()  // 권한 설정
                .requestMatchers(HttpMethod.POST, "/pets/add-pet").authenticated()  // POST 요청 시 "/pets/add-pet" 접근은 인증된 사용자만 허용
                .anyRequest().permitAll()  // 나머지 모든 요청은 인증 없이 허용
                .and()
                .httpBasic();  // HTTP Basic 인증 활성화

        return http.build();  // Spring Security 6에서는 http.build()를 호출해야 합니다.
    }
}
