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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomCorsConfiguration customCorsConfiguration) throws Exception {
        // grant authorization to users based on roles
        String[] everyRoles = {"AGENT", "MANAGER", "CUSTOMER"};
        String[] agentManagerRoles = {"AGENT", "MANAGER"};
        httpSecurity.authorizeHttpRequests(securityConfigurer ->
                securityConfigurer
                        .requestMatchers("/api/customers/register").permitAll()
                        .requestMatchers("/api/signup/agent").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/chat/**").permitAll() // for web socket
                        .requestMatchers("/uploads/**").permitAll() // for file hosting
                        // auth
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.GET, "/api/users/by-reference").hasAnyRole(everyRoles)
                        // agencies
                        .requestMatchers(HttpMethod.GET, "/agencies", "/api/agencies").permitAll()
                        // agents
                        .requestMatchers(HttpMethod.POST, "/agents").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agents/*/upload").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agents/*/photo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agents/me").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.PUT, "/agents/*").hasAnyRole(agentManagerRoles)
                        // booking
                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/bookings/create-payment-intent").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/bookings/cost-summary").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/bookings/confirm").hasAnyRole(everyRoles)
                        // booking details
                        .requestMatchers(HttpMethod.GET, "/api/bookingdetails").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.GET, "/api/bookingdetails/*").hasAnyRole(everyRoles)
                        // customers
                        .requestMatchers(HttpMethod.GET, "/api/customers").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.POST, "/api/customers/*/upload").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.GET, "/api/customers/*/photo").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.GET, "/api/customers/*").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.PUT, "/api/customers/*").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/customers/updatecustomer/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/delete/*").hasAnyRole(everyRoles)
                        // email
                        .requestMatchers(HttpMethod.POST, "/api/email/send").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/email/send-booking-confirmation").hasAnyRole(everyRoles)
                        // packages
                        .requestMatchers(HttpMethod.GET, "/packages").permitAll()
                        .requestMatchers(HttpMethod.GET, "/packages/*/details").permitAll()
                        .requestMatchers(HttpMethod.POST, "/packages").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.PUT, "/packages/*").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.DELETE, "/packages").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.GET, "/packages/product-supplier").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.GET, "/packages/search").permitAll()
                        // places
                        .requestMatchers(HttpMethod.GET, "/api/places/search", "/api/places/nearby").hasAnyRole(agentManagerRoles)
                        // products
                        .requestMatchers(HttpMethod.GET, "/api/products").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.GET, "/api/products/*").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.PUT, "/api/products/*").hasAnyRole(agentManagerRoles)
                        // ratings
                        .requestMatchers(HttpMethod.GET, "/api/ratings/*").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.POST, "/api/ratings").hasAnyRole(everyRoles)
                        // supplier contact
                        .requestMatchers(HttpMethod.GET, "/api/suppliercontacts").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.GET, "/api/suppliercontacts/*").hasAnyRole(agentManagerRoles)
                        .requestMatchers(HttpMethod.PUT, "/api/suppliercontacts/*").hasAnyRole(agentManagerRoles)
                        // wallet
                        .requestMatchers(HttpMethod.POST, "/api/wallet/topup").hasAnyRole(everyRoles)
                        .requestMatchers(HttpMethod.GET, "/api/wallet-balance").hasAnyRole(everyRoles)
        );

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                .configurationSource(customCorsConfiguration)
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
