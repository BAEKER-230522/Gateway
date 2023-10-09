package com.example.gateway.global.jwt;

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
public class JwtVerify {

    @Value("${custom.jwt.secret-key}")
    private String secretKeyPlain;
    private SecretKey cachedSecretKey;


    public void verify(String token) {
        try {
            isAvailable(token);
        }
        catch (ExpiredJwtException e) {throw new TokenValidException("토큰 만료");}
        catch (SignatureException e) {throw new TokenValidException("검증 되지않은 토큰");}
        catch (MalformedJwtException e) {throw new TokenValidException("토큰 구조 문제");}
    }

    private void isAvailable(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);
    }

    private SecretKey getSecretKey() {
        if (cachedSecretKey == null)
            cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64
                .getEncoder()
                .encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
}
