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

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
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

            List<SimpleGrantedAuthority> authorities;

            if (claims.containsKey("roles")) {
                List<?> rawRoles = claims.get("roles", List.class);
                List<String> roles = rawRoles.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());

                System.out.println("Roles from token (list): " + roles);

                authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            } else if (claims.containsKey("role")) {
                String role = claims.get("role").toString();
                System.out.println("Role from token (single): " + role);

                authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            } else {
                System.out.println("No role(s) found in token.");
                authorities = Collections.emptyList();
            }

            System.out.println("Authorities set in security context: " + authorities);

            if (username != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException e) {
            System.out.println("Invalid or expired JWT token");
        }

        filterChain.doFilter(request, response);
    }
}
