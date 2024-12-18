package est.commitdate.config;

import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import est.commitdate.service.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MemberRepository memberRepository) throws Exception {
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
                                        .requestMatchers("/login", "/sign-up", "/oauth2/additional-info").permitAll()
                                        .requestMatchers("/member/**").permitAll() // 중복 확인 관련 API 허용
                                        .requestMatchers("/users/**").hasAnyAuthority("MEMBER", "ADMIN")
                                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService(memberRepository)))
                        .successHandler((request, response, authentication) -> {
                            // OAuth 회원가입 후 추가 정보 입력 여부 검사
                            String email = authentication.getName();
                            Optional<Member> member = memberRepository.findByEmail(email);

//                            System.out.println("test" + member.get().isAdditionalInfoCompleted());
                             //member.isPresent() &&
                            log.info("member.toString()= {}", member.toString());
                            log.info("member.get().isAdditionalInfoCompleted() = {}", member.get().isAdditionalInfoCompleted());

                            if (member.get().isAdditionalInfoCompleted()) {
                                // 추가 정보 입력이 필요한 경우
                                System.out.println("if문이 실행");
                                response.sendRedirect("/additional-info");
                            } else {
                                // 추가 정보가 이미 존재하면 메인 페이지로 리디렉션
                                System.out.println("else가 실행");
                                response.sendRedirect("/");
                            }
                        })
//                        .failureHandler((request, response, exception) -> {
//                            log.warn("OAuth 로그인 실패: {}", exception.getMessage());
//                            response.sendRedirect("/oauth2/additional-info");
//                        })
                )
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