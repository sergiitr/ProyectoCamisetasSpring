// src/main/java/com/example/proyectoCamisetas/config/SecurityConfig.java
package com.example.proyectoCamisetas.config;

import com.example.proyectoCamisetas.entity.Usuario;
import com.example.proyectoCamisetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Implementación personalizada de UserDetailsService mediante una lambda.
     * Esta función define cómo Spring Security debe buscar usuarios en la BBDD.
     */
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return username -> {
            Usuario usuario = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            // Mapea el Enum 'tipo' a un rol de Spring Security (ROLE_...)
            String role = "ROLE_" + usuario.getTipo().name();

            return new User(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    Collections.singletonList(() -> role) // SimpleGrantedAuthority como lambda
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/cliente/**").permitAll()
                .requestMatchers("/admin/**").hasRole("OPERADOR")
                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    // Lógica de redirección
                    if (authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_OPERADOR"))) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/cliente"); // Redirige al índice del cliente
                    }
                })
            )
            
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}