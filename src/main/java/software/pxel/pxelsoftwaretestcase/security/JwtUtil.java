package software.pxel.pxelsoftwaretestcase.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final Long jwtExpiration;

    public JwtUtil(@Value("${jwt.secret}")String secret, @Value("${jwt.expiration}") Long jwtExpiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(Long userId){
        Date nowDate = new Date();
        Date expirationDate = new Date(nowDate.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(token)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            //log
            return false;
        }
    }
}
