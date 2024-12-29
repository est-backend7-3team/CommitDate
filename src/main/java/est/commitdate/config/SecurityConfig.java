package est.commitdate.config;

import est.commitdate.repository.MemberRepository;
import est.commitdate.service.member.CustomOAuth2UserService;

import est.commitdate.service.member.FormUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler successHandler;
    private final FormUserDetailsService formUserDetailsService;
    private final TempPasswordChangeHandler tempPasswordChangeHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MemberRepository memberRepository) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process-login")
                        .successHandler(tempPasswordChangeHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 루트 경로로 리디렉션
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                        .permitAll()
                )
                .authorizeHttpRequests(
                        auth ->
                                auth

                                        .requestMatchers("/css/**", "/js/**", "/images/**","image/profiles/**").permitAll()
                                        .requestMatchers("/login", "/sign-up", "/oauth2/additional-info", "update").permitAll()
                                        .requestMatchers("/member/forgot-password").permitAll()
                                        .requestMatchers("/member/**").permitAll() // 중복 확인 관련 API 허용
                                        .requestMatchers("/member/profile").authenticated()
                                        .requestMatchers("/users/**").hasAnyAuthority("MEMBER", "ADMIN")
                                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                        .requestMatchers("/board/**").permitAll()
                                        .requestMatchers("/post/**").permitAll()
                                        .requestMatchers("/post/test").permitAll()
                                        .requestMatchers("/swipe/**").permitAll()
                                        .requestMatchers("/chat/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService(memberRepository)))
                        .successHandler(successHandler))
                .userDetailsService(formUserDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(MemberRepository memberRepository) {
        return new CustomOAuth2UserService(memberRepository);
    }

}