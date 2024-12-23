package est.commitdate.service;

import org.springframework.security.core.context.SecurityContext;
import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.member.FormUserDetails;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.entity.Member;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SwipeService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public List<ChooseDto> getDummyChooseDTO(int volume){

        List<ChooseDto> chooseDTOList = new ArrayList<>();

        for (int i = 0; i < volume; i++) {
            chooseDTOList.add(ChooseDto.builder()
                                .userName("username"+i)
                                .comment("comment"+i)
                                .profileImageURL("https://via.placeholder.com/48")
                                .timestamp(1)
                                .build()
            );
        }

        return chooseDTOList;
    }

    //세션의 로그인 정보 추출
    public Member getLoggedInMember(HttpSession session) {
        // 시큐리티 컨텍스트에서 사용자 정보 추출
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        //시큐리티에 머 있으면 즉, 로그인을 한 것이라면
        if (securityContext != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails customUser) {
                return memberRepository.findById(customUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            } else if (principal instanceof FormUserDetails formUser) {
                return memberRepository.findById(formUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            }else{//로그인은 했는데 이상한것. RuntimeException 터트림.
                throw new RuntimeException("Unauthorized: Unable to identify the user");
            }
        }
        //손님 유저
        log.info("Anonymous");
        return null;

    }
}
