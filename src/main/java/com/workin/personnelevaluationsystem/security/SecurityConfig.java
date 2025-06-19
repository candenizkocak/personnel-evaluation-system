package com.workin.personnelevaluationsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/api/v1/**")
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Public access for static resources and authentication pages
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/home", "/login", "/register", "/error").permitAll()
                        .requestMatchers("/WEB-INF/**").permitAll()

                        // Authorize all web controller paths that require authentication.
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/departments/**").authenticated()
                        .requestMatchers("/positions/**").authenticated()
                        .requestMatchers("/employees/**").authenticated()
                        .requestMatchers("/evaluation-forms/**").authenticated()
                        .requestMatchers("/evaluation-periods/**").authenticated()
                        .requestMatchers("/evaluation-types/**").authenticated()
                        .requestMatchers("/question-types/**").authenticated()
                        .requestMatchers("/performance-reviews/**").authenticated()
                        .requestMatchers("/permissions/**").authenticated()
                        .requestMatchers("/roles/**").authenticated()
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/goal-types/**").authenticated()
                        .requestMatchers("/goal-statuses/**").authenticated()
                        .requestMatchers("/goals/**").authenticated()
                        .requestMatchers("/competency-categories/**").authenticated()
                        .requestMatchers("/competencies/**").authenticated()
                        .requestMatchers("/competency-levels/**").authenticated()
                        .requestMatchers("/employee-competencies/**").authenticated()
                        .requestMatchers("/feedback-types/**").authenticated() // <-- ADD THIS
                        .requestMatchers("/feedback/**").authenticated()       // <-- AND THIS
                        .requestMatchers("/notifications/**").authenticated()

                        // API endpoints
                        .requestMatchers("/api/v1/**").authenticated()

                        // Default rule: any other request must be authenticated.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}