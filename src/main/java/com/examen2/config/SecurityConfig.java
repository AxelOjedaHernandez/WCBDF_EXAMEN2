package com.examen2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;  // <-- Aquí está el import que falta

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()  // Deshabilitar CSRF
            .authorizeRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/promotions", "/api/promotions/{id}").hasAnyAuthority("READ", "UPDATE", "CREATE", "DELETE")
                .requestMatchers(HttpMethod.POST, "/api/promotions").hasAnyAuthority("CREATE", "ADMIN", "DEVELOPER", "EDITOR")
                .requestMatchers(HttpMethod.PUT, "/api/promotions/{id}").hasAnyAuthority("UPDATE", "ADMIN", "DEVELOPER", "MODERATOR")
                .requestMatchers(HttpMethod.DELETE, "/api/promotions/{id}").hasAnyAuthority("DELETE", "ADMIN", "DEVELOPER")
                .anyRequest().denyAll() // Negar cualquier otro acceso no autorizado
            )
            .httpBasic()  // Configuración básica HTTP
            .and()
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configuración de sesión sin estado
            ); // Utiliza el 'and()' para finalizar la configuración

        return httpSecurity.build();
    }

    // Bean para obtener el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean para el AuthenticationProvider, usando DaoAuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    // Bean para el PasswordEncoder (utilizando BCrypt para mayor seguridad)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Recomendado en producción para la seguridad
    }

    // Bean para el UserDetailsService, que configura los usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService() {
        // Definir los usuarios en memoria con sus roles y permisos
        UserDetails admin = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE", "CREATE_USER")
                .build();

        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .authorities("READ")
                .build();

        UserDetails moderator = org.springframework.security.core.userdetails.User
                .withUsername("moderator")
                .password(passwordEncoder().encode("mod123"))
                .roles("MODERATOR")
                .authorities("READ", "UPDATE")
                .build();

        UserDetails editor = org.springframework.security.core.userdetails.User
                .withUsername("editor")
                .password(passwordEncoder().encode("editor123"))
                .roles("EDITOR")
                .authorities("UPDATE")
                .build();

        UserDetails developer = org.springframework.security.core.userdetails.User
                .withUsername("developer")
                .password(passwordEncoder().encode("dev123"))
                .roles("DEVELOPER")
                .authorities("READ", "WRITE", "UPDATE", "DELETE", "CREATE_USER")
                .build();

        UserDetails analyst = org.springframework.security.core.userdetails.User
                .withUsername("analyst")
                .password(passwordEncoder().encode("analyst123"))
                .roles("ANALYST")
                .authorities("READ", "DELETE")
                .build();

        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(admin);
        userDetailsList.add(user);
        userDetailsList.add(moderator);
        userDetailsList.add(editor);
        userDetailsList.add(developer);
        userDetailsList.add(analyst);

        return new InMemoryUserDetailsManager(userDetailsList);
    }
}
