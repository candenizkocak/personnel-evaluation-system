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
                        .ignoringRequestMatchers("/api/v1/**") // Disable CSRF checks for all /api/v1 paths
                )
                .authorizeHttpRequests(authorize -> authorize
                        // IMPORTANT: Allow internal JSP paths to be accessed by the DispatcherServlet without security checks
                        .requestMatchers("/WEB-INF/**").permitAll() // <--- ADD THIS LINE HERE

                        // Public access for static resources and authentication pages
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/", "/home", "/login", "/register", "/error").permitAll()

                        // API endpoints: require authentication (and specific roles/permissions)
                        .requestMatchers("/api/v1/departments/**").hasAnyRole("ADMIN", "HR_SPECIALIST")
                        .requestMatchers("/api/v1/employees/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "MANAGER")
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/roles/**", "/api/v1/permissions/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/evaluation-periods/**", "/api/v1/evaluation-types/**", "/api/v1/question-types/**").hasAnyRole("ADMIN", "HR_SPECIALIST")
                        .requestMatchers("/api/v1/evaluation-forms/**", "/api/v1/evaluation-questions/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "MANAGER")
                        .requestMatchers("/api/v1/performance-reviews/**", "/api/v1/review-responses/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "MANAGER")
                        .requestMatchers("/api/v1/competency-categories/**", "/api/v1/competencies/**", "/api/v1/competency-levels/**", "/api/v1/employee-competencies/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "MANAGER")
                        .requestMatchers("/api/v1/goal-types/**", "/api/v1/goal-statuses/**", "/api/v1/goals/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "MANAGER")
                        .requestMatchers("/api/v1/feedback-types/**", "/api/v1/feedback/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/v1/notifications/**").hasAnyRole("ADMIN", "HR_SPECIALIST", "EMPLOYEE", "MANAGER")
                        .anyRequest().authenticated() // All other paths require authentication by default
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