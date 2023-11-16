package org.group23.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Returns a builder used for MVC matcher patterns.
     * This is required to remove ambiguity due to a vulnerability.
     * @param introspector the introspector, automatically injected
     * @return the builder to use when creating patterns
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * Returns the password encoder to use for this application.
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Returns the application's user details service.
     * This service is responsible for retrieving user information for authentication.
     * @return the user details service
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Manually defines three users.
        UserDetails user1 = User.withUsername("Andrei")
                .password(passwordEncoder().encode("Andrei"))
                .roles("USER")
                .build();
        UserDetails user2 = User.withUsername("Cam")
                .password(passwordEncoder().encode("Cam"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                // Allow all requests to the home page
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(mvc.pattern("/")).permitAll()
                        .anyRequest().authenticated()
                )
                // Define the login path, and allow all requests to it
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                // Allow all logouts
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
