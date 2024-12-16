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
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process-login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 루트 경로로 리디렉션
                        .permitAll()
                )
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                        .requestMatchers("/login", "/sign-up").permitAll()
                                        .requestMatchers("/member/sign-up",
                                                "/member/check-email",
                                                "/member/check-nickname",
                                                "/member/check-phone-number").permitAll() // 중복 확인 API 허용
                                        .requestMatchers("/users/**")
                                        .hasAnyAuthority("MEMBER", "ADMIN")
                                        .requestMatchers("/admin/**")
                                        .hasAnyAuthority("ADMIN")
                                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}