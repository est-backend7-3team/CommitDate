package est.commitdate.service;

import est.commitdate.dto.CustomUserDetails;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = extractEmail(oAuth2User, registrationId);

        Member findMember = memberRepository.findByEmail(email).orElseGet(() -> {
            Member tempMember = Member.builder()
                    .email(email)
                    .provider(registrationId)
                    .role("USER")
                    .username("Pending")
                    .nickname("Pending")
                    .phoneNumber("Pending")
                    .additionalInfoCompleted(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return memberRepository.save(tempMember);
        });

        log.info("findMember.isAdditionalInfoCompleted() = {}", findMember.isAdditionalInfoCompleted());
        //return oAuth2User;
        return new CustomUserDetails(findMember.getUsername(), findMember.getEmail(), findMember.getRole(), findMember.isAdditionalInfoCompleted(), oAuth2User.getAttributes());
    }

    private String extractEmail(OAuth2User oAuth2User, String registrationId) {

        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("email");
            case "naver" -> ((Map<String, String>) oAuth2User.getAttribute("response")).get("email");
            case "kakao" -> ((Map<String, Object>) oAuth2User.getAttribute("kakao_account")).get("email").toString();
            default -> throw new IllegalArgumentException("등록되지않은 유저ID: " + registrationId);
        };

    }

}