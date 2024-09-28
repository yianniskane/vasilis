package com.directory.controller;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {
//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//    @Lazy(false)
//    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
//        CriteriaBuilderConfiguration config = Criteria.getDefault();
//        // do some configuration
//        return config.createCriteriaBuilderFactory(entityManagerFactory);
//    }

    private final UserDetailsService userDetailsService;
    private final CustomUsernameSessionFilter customUsernameSessionFilter;



    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, CustomUsernameSessionFilter customUsernameSessionFilter) {
        this.userDetailsService = userDetailsService;
        this.customUsernameSessionFilter = customUsernameSessionFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use as required
                ).addFilterBefore(customUsernameSessionFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        // Permit access to these endpoints without authentication
                        .requestMatchers( "/pages-login", "/pages-register/**").permitAll()
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                ).formLogin((form) -> form
                        .loginPage("/pages-login")  // Custom login page
                        .defaultSuccessUrl("/main", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
//                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authenticationProvider(authenticationProvider());

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(this.userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}