package com.example.proyectoCamisetas.config;

import com.example.proyectoCamisetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

    /**
     * Define el cifrador de contraseñas, necesario para verificar las contraseñas de la BBDD.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Implementación simplificada de UserDetailsService (Busca el usuario en la BBDD).
     * Usa .map() y .orElseThrow() sobre el Optional devuelto por el repositorio.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
            .map(usuario -> {
                // Obtenemos el rol y lo convertimos a formato de Spring Security
                String role = "ROLE_" + usuario.getTipo().name(); 
                
                // Creamos el objeto UserDetails con el nombre, contraseña y rol.
                return new User(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
                );
            })
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username)); // Lanza error si no lo encuentra
    }

    /**
     * Definimos la cadena de filtros de seguridad HTTP, configurando las reglas de autorización, el proceso de login y el logout.
     * Esta función es esencial para el control de acceso en la aplicación, redirigiendo a los usuarios basándose en su rol después de un login exitoso.
     * @param http El objeto HttpSecurity proporcionado por Spring para configurar la seguridad.
     * @return Una cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas accesibles por cualquiera (público)
                .requestMatchers("/", "/login", "/register", "/cliente/**").permitAll()
                // Zona de Admin: Requiere el rol OPERADOR
                .requestMatchers("/admin/**").hasRole("OPERADOR")
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            
            // Configuramos formulario y manejo de redirección.
            .formLogin(form -> form
                .loginPage("/login") 
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    // Comprueba si el usuario tiene el rol OPERADOR
                    boolean isOperador = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_OPERADOR"));   
                    if (isOperador)
                        response.sendRedirect("/admin");
                    else
                        response.sendRedirect("/cliente");
                })
            )
            
            // Configuramos el cierre de sesión.
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}