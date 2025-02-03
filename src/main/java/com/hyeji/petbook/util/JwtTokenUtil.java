package com.hyeji.petbook.util;

import com.hyeji.petbook.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    // 비밀 키 (실제 배포 시 더 안전한 방법으로 관리해야 함)
    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 토큰 만료 시간 설정 (예: 1시간)
    private long expirationTime = 1000 * 60 * 60;

    // JWT 토큰 생성
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // 사용자의 이메일을 토큰의 subject로 설정
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 서명 알고리즘과 비밀 키
                .compact(); // 최종적으로 토큰 생성
    }

    // JWT 토큰에서 사용자 정보 추출 (이메일 등)
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // 서명 키 설정
                .parseClaimsJws(token) // 토큰을 파싱하여 클레임 얻기
                .getBody()
                .getSubject(); // 이메일(또는 username)을 반환
    }

    // JWT 토큰이 만료됐는지 확인
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    // JWT 토큰 검증 (유효성 검사)
    public boolean validateToken(String token, User user) {
        String username = getUsernameFromToken(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

}
