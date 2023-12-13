package ca.gbc.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
// Marks this class as a configuration source and enables WebFlux security
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    // Injects the 'issuer-uri' value from application properties

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer_url;
    // Bean to create a Reactive JWT Decoder based on the issuer URL

    @Bean
    public ReactiveJwtDecoder jwtDecoder()
    // Constructs a JWT decoder that uses the provided issuer URL
    {
        return ReactiveJwtDecoders.fromIssuerLocation(issuer_url);
    }
    // Bean to define the security web filter chain
    @Bean
    public SecurityWebFilterChain sprintSecurityWebFilterChain(ServerHttpSecurity httpSecurity) {
        // Configures HTTP security
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeExchange( // Permits all requests to paths matching '/eureka/**'

                        exchange -> exchange.pathMatchers("/eureka/**").permitAll().anyExchange().authenticated())
                .oauth2ResourceServer(configurer -> {
                    configurer.jwt(jwt -> {
                        jwt.jwtDecoder(jwtDecoder());
                    });
                });
        // Builds and returns the security filter chain
        return httpSecurity.build();
    }
}
