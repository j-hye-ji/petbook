package com.hyeji.petbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    // AuthenticationManager 정의 (Spring Security 6에서는 이를 이렇게 설정합니다)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Spring Security 6에서는 authenticationManager를 HttpSecurity에서 얻는 방식으로 설정
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())  // UserDetailsService 연결
                .passwordEncoder(passwordEncoder())  // PasswordEncoder 연결
                .and()
                .build();  // AuthenticationManager를 생성하고 반환
    }

    // SecurityFilterChain 사용하여 보안 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeHttpRequests()  // 권한 설정
                .requestMatchers("/pets/add-pet").authenticated()  // 인증된 사용자만 접근 가능
                .anyRequest().permitAll()  // 나머지 모든 요청은 허용
                .and()
                .httpBasic();  // HTTP Basic 인증 활성화

        return http.build();  // Spring Security 6에서는 http.build() 호출 필요
    }
}
