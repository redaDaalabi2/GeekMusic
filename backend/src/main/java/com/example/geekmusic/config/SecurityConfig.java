package com.example.geekmusic.config;

import com.example.geekmusic.config.usersDetails.AuteurDetailsService;
import com.example.geekmusic.config.usersDetails.AuditeurDetailsService;
import com.example.geekmusic.exception.InvalidCredentialsException;
import com.example.geekmusic.model.entities.AppUser;
import com.example.geekmusic.model.entities.Auteur;
import com.example.geekmusic.model.repo.AuteurRepo;
import com.example.geekmusic.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;
    private final AuditeurDetailsService auditeurDetailsService;
    private final AuteurDetailsService auteurDetailsService;
    private final AuteurRepo auteurRepo;

    @Autowired
    @Lazy
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserService userService,
                          AuteurDetailsService auteurDetailsService,
                          AuditeurDetailsService auditeurDetailsService,
                          AuteurRepo auteurRepo) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.auteurDetailsService = auteurDetailsService;
        this.auditeurDetailsService = auditeurDetailsService;
        this.auteurRepo = auteurRepo;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .and()
                .authorizeHttpRequests(aut ->
                        aut.requestMatchers("/patient/**").hasAuthority("PATIENT")
                )
                .authorizeHttpRequests(aut ->
                        aut.requestMatchers("/doctor/**").hasAuthority("DOCTOR")
                )
                .authorizeHttpRequests(aut ->
                        aut.requestMatchers("/secretaire/**").hasAuthority("SECRETERE")
                )
                .authorizeHttpRequests(aut ->
                        aut.requestMatchers("/admin/**").hasAuthority("ADMIN")
                )
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                if (email.endsWith("-PATIENT")) {
                    String emailOnly = email.substring(0, email.length() - 8);
                    AppUser user = userService.findUserByEmail(emailOnly);
                    if (user == null) {
                        throw new InvalidCredentialsException("invalid credentials");
                    }
                    return new User(user.getEmail(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("PATIENT")));

                } else if (email.endsWith("-DOCTOR"))  {
                    String emailOnly = email.substring(0, email.length() - 7);
                    System.out.println(emailOnly);
                    Auteur user = auteurRepo.getAuteurByEmail(emailOnly);
                    if (user == null) {
                        throw new InvalidCredentialsException("invalid credentials");
                    }

                    return new User(user.getEmail(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("DOCTOR")));
                }

                throw new InvalidCredentialsException("invalid credentials");

            }
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }

}
