package com.examen2.config;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    // Security Filter Chain Configuration
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .httpBasic(Customizer.withDefaults())  // Autenticación básica
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Sin estado
            .csrf().disable()  // Deshabilitar CSRF para una API REST
            .authorizeHttpRequests(http -> {
                // Permitir acceso a Swagger UI y OpenAPI sin autenticación
                http.requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll();

                // Configurar permisos específicos para los endpoints de tu API
                http.requestMatchers(HttpMethod.GET, "/api/promotions").hasAnyAuthority("READ");
                http.requestMatchers(HttpMethod.GET, "/api/promotions/{id}").hasAnyAuthority("READ");
                http.requestMatchers(HttpMethod.POST, "/api/promotions").hasAnyAuthority("CREATE");
                http.requestMatchers(HttpMethod.PUT, "/api/promotions/{id}").hasAnyAuthority("UPDATE");
                http.requestMatchers(HttpMethod.DELETE, "/api/promotions/{id}").hasAnyAuthority("DELETE");

                // Cualquier otra solicitud requiere autenticación
                http.anyRequest().authenticated();
            })
            .build();
}

    // AuthenticationManager Configuration
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // AuthenticationProvider Configuration
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());  // Configurar codificador de contraseñas
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());  // Configurar servicio de detalles de usuario
        return daoAuthenticationProvider;
    }

    // Password Encoder Configuration
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // UserDetailsService Configuration
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin1234"))
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE")
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user1234"))
                .roles("USER")
                .authorities("READ")
                .build();

        UserDetails moderator = User.withUsername("moderator")
                .password(passwordEncoder().encode("mod1234"))
                .roles("MODERATOR")
                .authorities("READ", "UPDATE")
                .build();

        UserDetails editor = User.withUsername("editor")
                .password(passwordEncoder().encode("editor1234"))
                .roles("EDITOR")
                .authorities("UPDATE")
                .build();

        UserDetails developer = User.withUsername("developer")
                .password(passwordEncoder().encode("dev1234"))
                .roles("DEVELOPER")
                .authorities("READ", "WRITE", "UPDATE", "DELETE", "CREATE_USER")
                .build();

        UserDetails analyst = User.withUsername("analyst")
                .password(passwordEncoder().encode("analyst1234"))
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

        return new InMemoryUserDetailsManager(userDetailsList);  // Usar usuarios en memoria
    }
}
