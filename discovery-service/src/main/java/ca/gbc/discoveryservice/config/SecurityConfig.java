package ca.gbc.discoveryservice.config;


// Required imports
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Configuration annotation to mark this class as a configuration source
@Configuration
// Enable Spring Web Security
@EnableWebSecurity
public class SecurityConfig {
    // Bean annotation to expose this method as a bean in the Spring context
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Configuring HttpSecurity
        // Disabling CSRF (Cross-Site Request Forgery) protection.
        // This is often done for APIs or services where clients are not using cookies.
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Specifying that any request should be allowed.
                        // This means no authentication is required to access any endpoint.
                        .anyRequest()
                        .permitAll());
        // Building and returning the SecurityFilterChain instance
        return http.build();
    }
}
