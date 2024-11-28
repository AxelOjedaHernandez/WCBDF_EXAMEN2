package com.examen2.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Permitir acceso a la documentación de la API
                    http.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();

                    // Aquí agregas las reglas para tus endpoints específicos
                    http.requestMatchers(HttpMethod.GET, "/api/v1/promotions/read").hasAnyAuthority("READ");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/promotions/create").hasAnyAuthority("CREATE");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/promotions/update").hasAnyAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/promotions/delete").hasAnyAuthority("DELETE");


                    // Denegar todas las demás rutas
                    http.anyRequest().denyAll();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
        .password("admin123")
        .roles("ADMIN")
        .authorities("READ", "CREATE", "UPDATE", "DELETE")
        .build();
    
        UserDetails user = User.withUsername("user")
                .password("user123")
                .roles("USER")
                .authorities("READ")
                .build();
    
        UserDetails moderator = User.withUsername("moderator")
                .password("moderator123")
                .roles("MODERATOR")
                .authorities("READ", "UPDATE")
                .build();
    
        UserDetails editor = User.withUsername("editor")
                .password("editor123")
                .roles("EDITOR")
                .authorities("CREATE", "UPDATE")
                .build();
    
        UserDetails developer = User.withUsername("developer")
                .password("developer123")
                .roles("DEVELOPER")
                .authorities("READ", "CREATE", "UPDATE", "DELETE")
                .build();
    
        UserDetails analyst = User.withUsername("analyst")
                .password("analyst123")
                .roles("ANALYST")
                .authorities("READ", "DELETE")
                .build();
    
        return new InMemoryUserDetailsManager(admin, user, moderator, editor, developer, analyst);
    }
    
}
