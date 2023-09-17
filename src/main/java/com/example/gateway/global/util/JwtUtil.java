package com.example.gateway.global.util;

import com.example.gateway.global.error.exception.token.TokenValidException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtUtil {

    private SecretKey cachedSecretKey;
    @Value("${custom.jwt.secret-key}")
    private String secretKeyPlain;

    private SecretKey _getSecretKey() {
        // 키를 Base64 인코딩함
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        // 인코딩된 키를 이용하여 SecretKey 객체 생성
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException e) {
            throw new TokenValidException("토큰 만료 ");
        } catch (SignatureException e) {
            throw new TokenValidException("검증 되지않은 토큰");
        } catch (MalformedJwtException e) {
            throw new TokenValidException("토큰 구조 문제");
        } catch (IllegalArgumentException e) {
            throw new TokenValidException("토큰이 존재하지 않음");
        }
        return true;
    }

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();

        return cachedSecretKey;
    }
}

