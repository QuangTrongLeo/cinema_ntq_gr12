package ntq.cinema.movie_module.security;

import ntq.cinema.auth_module.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class GenreSecurity {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.ntq-cinema-url}")
    private String cinemaApiBaseUrl;

    @Autowired
    public GenreSecurity(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChainGenre(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(cinemaApiBaseUrl + "/genres/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, cinemaApiBaseUrl + "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, cinemaApiBaseUrl + "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, cinemaApiBaseUrl + "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, cinemaApiBaseUrl + "/genres/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerGenre(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
