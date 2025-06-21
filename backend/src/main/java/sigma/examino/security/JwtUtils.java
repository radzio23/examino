package sigma.examino.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * Klasa narzędziowa do generowania i weryfikowania tokenów JWT.
 * Wykorzystuje bibliotekę JJWT oraz symetryczne klucze HMAC.
 */
public class JwtUtils {

    /**
     * Sekretny klucz do podpisywania tokenów JWT (musi mieć min. 32 znaki).
     */
    private static final String SECRET = "toJestMojSekretnyKluczDoJwtKtoryMaMin32Znaki!";

    /**
     * Klucz HMAC generowany na podstawie tajnego ciągu znaków.
     */
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Czas ważności tokenu w milisekundach (1 godzina).
     */
    private static final long expirationMs = 3600000;

    /**
     * Generuje nowy token JWT na podstawie nazwy użytkownika i jego roli.
     *
     * @param username nazwa użytkownika
     * @param role     rola użytkownika ( "STUDENT" lub "ADMIN")
     * @return podpisany token JWT jako ciąg znaków
     */
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Parsuje i weryfikuje token JWT.
     *
     * @param token token JWT do zweryfikowania
     * @return obiekt {@link Jws} zawierający roszczenia (claims)
     * @throws JwtException jeśli token jest nieprawidłowy lub wygasł
     */
    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
