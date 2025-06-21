package sigma.examino.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguracja zabezpieczeń używana podczas testów.
 * <p>
 * Wyłącza CSRF oraz zezwala na dostęp do wszystkich endpointów bez autoryzacji,
 * aby uprościć testowanie kontrolerów bez uwierzytelniania.
 */
@TestConfiguration
public class TestSecurityConfig {

    /**
     * Tworzy filtr bezpieczeństwa na potrzeby testów, który:
     * <ul>
     *   <li>wyłącza CSRF</li>
     *   <li>zezwala na dostęp do wszystkich żądań</li>
     * </ul>
     *
     * @param http obiekt konfiguracji zabezpieczeń
     * @return skonfigurowany łańcuch filtrów
     * @throws Exception jeśli konfiguracja zakończy się błędem
     */
    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // wszystko dostępne

        return http.build();
    }
}

