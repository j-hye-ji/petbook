package com.hyeji.petbook.util;

import com.hyeji.petbook.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    // application.properties에서 비밀 키를 읽어옵니다.
    @Value("${jwt.secret-key}")
    private String secretKeyString;

    // JWT 토큰 만료 시간 (1시간) (application.properties에서 가져오도록 변경 가능)
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    // 비밀 키를 안전하게 반환하는 메소드
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // JWT 토큰 생성
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())  // 사용자의 이메일을 토큰의 subject로 설정
                .claim("role", user.getUserRole().name())  // 사용자의 역할 설정
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 만료 시간
                .signWith(getSecretKey())  // 서명 시 비밀 키 사용
                .compact();  // 최종적으로 토큰 생성
    }

    // JWT 토큰에서 사용자 정보 추출 (이메일 등)
    public String getUsernameFromToken(String token) {
        try {
            // "Bearer " 접두사를 제거하고, 공백을 모두 제거한 후 파싱
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);  // "Bearer "를 제거하고 토큰만 남김
            }

            // 공백 제거
            token = token.replaceAll("\\s", "");

            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())  // 서명 키 설정
                    .build()
                    .parseClaimsJws(token)  // 토큰 파싱
                    .getBody()
                    .getSubject();  // 이메일을 반환
        } catch (JwtException | IllegalArgumentException e) {
            // 예외 처리 (잘못된 토큰 형식, 만료된 토큰 등)
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " 제거
            }
            token = token.replaceAll("\\s", ""); // 공백 제거

            // JWT 토큰 파싱
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()) // 비밀 키로 서명 검증
                    .build()
                    .parseClaimsJws(token) // 토큰 파싱
                    .getBody()
                    .get("userId", Long.class);  // claim에서 userId 추출
        } catch (JwtException e) {
            System.err.println("JWT 토큰 파싱 오류: " + e.getMessage());  // 로그 추가
            throw new IllegalArgumentException("유효하지 않은 토큰입니다."); // 예외 던지기
        } catch (IllegalArgumentException e) {
            System.err.println("토큰 처리 오류: " + e.getMessage());  // 로그 추가
            throw new IllegalArgumentException("토큰 형식이 잘못되었습니다."); // 예외 던지기
        }
    }


    // JWT 토큰이 만료되었는지 확인
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())  // 서명 키 설정
                .build()
                .parseClaimsJws(token.trim())  // 공백 제거 후 토큰 파싱
                .getBody()
                .getExpiration();  // 만료 시간
        return expiration.before(new Date());  // 만료 여부 체크
    }

    public boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));  // 이메일 비교와 만료 여부 확인
    }

    // JWT 토큰에서 Claims 정보 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())  // 서명 키 설정
                .build()
                .parseClaimsJws(token.trim())  // 공백 제거 후 토큰 파싱
                .getBody();  // Claims 반환
    }
}
