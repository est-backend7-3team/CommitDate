package est.commitdate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
//
                .logout(Customizer.withDefaults())
//
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/login", "/sign-up")
                                        .anonymous()
                                        .requestMatchers("/users/**")
                                        .hasAnyAuthority("MEMBER","ADMIN")
                                        .requestMatchers("/manager/**")
                                        .hasAnyAuthority("ADMIN")
                                        .requestMatchers("/admin/**")
                                        .hasAnyAuthority("ADMIN")
                                        .anyRequest()
                                        .authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
