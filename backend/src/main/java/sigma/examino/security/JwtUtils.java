package sigma.examino.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

private static final String SECRET = "toJestMojSekretnyKluczDoJwtKtoryMaMin32Znaki!";
private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long expirationMs = 3600000; // 1 godzina ważności tokenu

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
