package est.commitdate.service;

import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = extractEmail(oAuth2User, registrationId);

        memberRepository.findByEmail(email).orElseGet(() -> {
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
            System.out.println("member가입됨");
            return memberRepository.save(tempMember);
        });
        return oAuth2User;
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












//        // 이메일이 없으면 OAuthExtraInfoController에서 처리함
//        if (email == null || email.isEmpty()) {
//            throw new IllegalStateException("이메일이 제공되지 않았습니다. 추가 정보를 입력하세요.");
//        }

//        Optional<Member> existingMember = memberRepository.findByEmail(email);
//
//        if (existingMember.isEmpty()) {
//            // 이메일만으로 임시 회원 저장
//            Member member = Member.builder()
//                    .email(email)
//                    .username(oAuth2User.getAttribute(
//                            "name") != null ? oAuth2User.getAttribute("name") : "No Name")
//                    .role("USER")
//                    .provider(registrationId)
//                    .build();
//
//            memberRepository.save(member);
//        }
//
//        return oAuth2User;
//    }
//
//    // 이메일 추출 메서드
//    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
//        Map<String, String> emailPaths = Map.of(
//                "google", "email",
//                "naver", "response.email",
//                "kakao", "kakao_account.email"
//        );
//
//        if (!emailPaths.containsKey(registrationId.toLowerCase())) {
//            throw new IllegalArgumentException("Unknown registrationId: " + registrationId);
//        }
//
//        String emailPath = emailPaths.get(registrationId.toLowerCase());
//        return extractNestedAttribute(oAuth2User, emailPath)
//                .orElseThrow(() -> new IllegalArgumentException(registrationId + " 이메일이 제공되지 않았습니다."));
//    }
//
//    // 중첩된 속성 추출 메서드
//    private Optional<String> extractNestedAttribute(OAuth2User oAuth2User, String path) {
//        String[] keys = path.split("\\.");
//        log.info("KEYS: " + Arrays.toString(keys));
//
//        Object current = oAuth2User;
//
////        String email = null;
//
//        for (String key : keys) {
//
////            Object target = oAuth2User.getAttribute(key);
////
////            if (target instanceof Map) {
////                email = ((Map<String, String>) target).get("email");
////            } else {
////                email = target.toString();
////            }
//
//            if (current instanceof Map) {
//                current = ((Map<?, ?>) current).get(key);
//            } else {
//                return Optional.empty();
//            }
//        }
//
//        return Optional.ofNullable(current).map(Object::toString);
//    }
