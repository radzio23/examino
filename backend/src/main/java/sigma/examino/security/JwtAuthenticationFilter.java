package sigma.examino.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtr uwierzytelniania JWT, wykonywany raz dla każdego żądania.
 * Odpowiada za:
 * <ul>
 *     <li>odczyt nagłówka Authorization,</li>
 *     <li>weryfikację poprawności tokena JWT,</li>
 *     <li>ustawienie informacji o użytkowniku w kontekście Spring Security.</li>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Przetwarza żądanie HTTP i filtruje je pod kątem poprawności tokena JWT.
     * Jeśli token jest poprawny, ustawia kontekst uwierzytelnienia użytkownika.
     *
     * @param request      obiekt żądania HTTP
     * @param response     obiekt odpowiedzi HTTP
     * @param filterChain  łańcuch filtrów do kontynuowania przetwarzania
     * @throws ServletException w przypadku błędu serwletu
     * @throws IOException       w przypadku błędu wejścia/wyjścia
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Zawsze czyścimy kontekst bezpieczeństwa na początku
        SecurityContextHolder.clearContext();

        String header = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("Authorization header: " + header);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(TOKEN_PREFIX.length());

        try {
            Jws<Claims> claimsJws = JwtUtils.parseToken(token);
            Claims claims = claimsJws.getBody();

            String username = claims.getSubject();
            System.out.println("Authenticated username: " + username);

            List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

            if (username != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException e) {
            System.out.println("Invalid or expired JWT token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Ekstrahuje role użytkownika z tokena JWT i przekształca je na uprawnienia Spring Security.
     *
     * @param claims ciało tokena JWT
     * @return lista ról w postaci {@link SimpleGrantedAuthority}
     */
    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        if (claims.containsKey("roles")) {
            List<?> rawRoles = claims.get("roles", List.class);
            List<String> roles = rawRoles.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            System.out.println("Roles from token (list): " + roles);

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
            System.out.println("Extracted authorities: " + authorities);
            return authorities;
        } else if (claims.containsKey("role")) {
            String role = claims.get("role").toString();
            System.out.println("Role from token (single): " + role);
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            System.out.println("Extracted authorities: " + authorities);
            return authorities;
        } else {
            System.out.println("No role(s) found in token.");
            return Collections.emptyList();
        }
    }
}
