package ge.join2play.join2playback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/events/setup").permitAll()
                .requestMatchers("/api/events").permitAll()
                .requestMatchers("/api/events/{id}").permitAll()
                .requestMatchers("/api/tournaments").permitAll()
                .requestMatchers("/api/tournaments/{id}").permitAll()
                .requestMatchers("/api/users/search").permitAll()
                .requestMatchers("/api/users/details/{id}").permitAll()
                .requestMatchers("/api/users/events/hosted/{id}").permitAll()
                .requestMatchers("/api/users/events/registered/{id}").permitAll()
                .requestMatchers("/api/teams/{id}").permitAll()
                .requestMatchers("/api/teams/available").permitAll()
                .requestMatchers("/api/team-requests/check-request/**").permitAll()
                .requestMatchers("/api/event-invitations/check-invitation/**").permitAll()
                .requestMatchers("/api/friends/check-request/**").permitAll()
                .requestMatchers("/api/tournament-registrations/tournament/{tournamentId}/all").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow your frontend origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        
        // Allow credentials (essential for session cookies)
        configuration.setAllowCredentials(true);
        
        // Allow all common HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Allow specific headers instead of wildcard when using credentials
        configuration.setAllowedHeaders(Arrays.asList(
            "Accept", 
            "Authorization", 
            "Content-Type", 
            "X-Requested-With", 
            "Cache-Control"
        ));
        
        // Expose headers needed for session handling
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "JSESSIONID"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
