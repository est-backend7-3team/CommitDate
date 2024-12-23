package est.commitdate.service.member;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.member.OAuthSignUpRequest;
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

import java.util.Map;

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

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email).orElseGet(() -> {
            // 이메일이 없는 경우, 임시 사용자 생성
            OAuthSignUpRequest request = new OAuthSignUpRequest(
                    email,"Pending", "Pending", "Pending");
            return memberRepository.save(request.toEntity(registrationId));
        });

        // CustomUserDetails로 반환
        return new CustomUserDetails(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getRole(),
                member.isAdditionalInfoCompleted(),
                oAuth2User.getAttributes(),
                member.getId()
        );
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