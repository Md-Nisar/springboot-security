package com.mna.springbootsecurity.security.util;

import com.mna.springbootsecurity.base.constant.HTTPHeaders;
import com.mna.springbootsecurity.base.property.JwtProperties;
import com.mna.springbootsecurity.security.exception.JwtSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private final SecretKey signInKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        byte[] keyBytes = Decoders.BASE64.decode(this.jwtProperties.getSecret());
        this.signInKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationTime()))
                .signWith(signInKey)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationTime()))
                .signWith(signInKey)
                .compact();
    }

    public boolean validateToken(String token, String subject) {
        final String tokenSubject = extractSubject(token);
        return (tokenSubject.equals(subject) && !isTokenExpired(token));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signInKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new JwtSignatureException("An error occurred while parsing the JWT token.");
        }

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateTokenWithClaims(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationTime()))
                .signWith(signInKey)
                .compact();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HTTPHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(jwtProperties.getTokenPrefix())) {
            return header.replace(jwtProperties.getTokenPrefix(), "");
        }
        return null;
    }

}


