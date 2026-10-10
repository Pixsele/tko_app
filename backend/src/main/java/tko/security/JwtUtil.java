package tko.security;


import tko.database.entity.user.UsersEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    //TODO
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UsersEntity user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(Date.from(Instant.now()))
                //TODO
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UsersEntity user) {
        final String login = extractLogin(token);
        return (login.equals(user.getLogin()) && !isTokenExpired(token));

    }
}
