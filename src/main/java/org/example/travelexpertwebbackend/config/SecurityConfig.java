package org.example.travelexpertwebbackend.config;


import org.example.travelexpertwebbackend.security.JwtAuthenticationFilter;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // define authentication config for Spring Security
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // set user details service
        provider.setUserDetailsService(userService);
        // set password encoder
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }


    // Security Config using filter chains
    // configure roles
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//         grant authorization to users based on roles
        httpSecurity.authorizeHttpRequests(securityConfigurer ->
                securityConfigurer
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/signup/agent").permitAll() //TODO: change to admin or manager later
                        .requestMatchers("/api/login").permitAll()
                        // agencies
                        .requestMatchers(HttpMethod.GET, "/agencies").permitAll()
                        // agents
                        .requestMatchers(HttpMethod.GET, "/agents/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/agents").permitAll() // TODO: change to admin or manager later
                        .requestMatchers(HttpMethod.GET, "/api/customers").permitAll()
                        // packages
                        // TODO: change to agent later
                        .requestMatchers(HttpMethod.GET, "/packages").permitAll()
                        .requestMatchers(HttpMethod.POST, "/packages").permitAll()
                        .requestMatchers(HttpMethod.GET, "/packages/product-supplier").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/packages/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/packages/search/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/packages/search").permitAll()

                        // product
                        // TODO: change to agent later
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()

                        // supplier contact
                        // TODO: change to agent later
                        .requestMatchers(HttpMethod.GET, "/api/suppliercontacts").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/suppliercontacts/*").permitAll()

        );

        httpSecurity.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        // disable CSRF for testing purposes
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
