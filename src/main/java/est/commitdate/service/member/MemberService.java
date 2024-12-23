package est.commitdate.service.member;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.member.FormUserDetails;
import est.commitdate.dto.member.MemberDetailRequest;
import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.exception.*;
import est.commitdate.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                ()-> new MemberNotFoundException("해당 회원을 찾을 수 없습니다.")
        );
    }

    public Member getMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow(
                ()-> new MemberNotFoundException("해당 회원을 찾을 수 없습니다.")
        );
    }

    @Transactional
    public void signUp(MemberSignUpRequest request) {
        // 이메일 중복 여부 확인
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicatedEmailException("이미 가입된 이메일 입니다.");
        }

        // 닉네임 중복 여부 확인
        if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicatedNicknameException("이미 가입된 닉네임입니다.");
        }

        // 전화번호 중복 여부 확인
        if (memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new DuplicatedPhoneNumberException("이미 가입된 전화번호입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // 만약 닉네임이 admin이라면 admin계정생성
        if (request.getNickname().equals("ADMIN")) {
            log.info("관리자 계정 생성");
            Member admin = request.toAdminEntity(encryptedPassword);
            memberRepository.save(admin);
        } else {
            // MemberSignUpRequest 에서 엔티티 생성
            Member member = request.toEntity(encryptedPassword);
            log.info("일반 회원 계정 생성");
            memberRepository.save(member);
        }

//        System.out.println(" 회원가입 완료! : " + member.toString());
    }

    @Transactional
    public Member login(String email, String rawPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    @Transactional
    public void updateMemberDetails(Long memberId, MemberDetailRequest request) {
        Member member = getMemberById(memberId);

        // 비밀번호 변경 여부 확인
        String encryptedPassword = null;
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            encryptedPassword = passwordEncoder.encode(request.getPassword());
        }

        // DTO에서 엔티티로 값 적용
        request.applyToMember(member, encryptedPassword);
    }

    @Transactional
    public void delete(Long id) {
        Member findMember = getMemberById(id);
        findMember.delete();
        System.out.println("탈퇴 완료." + findMember.getStatus()+ " " + findMember.getUsername());
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
        return null;
    }

    // 해당 계정의 정보가 ADMIN인지, MEMBER인지 반환
    public Boolean AuthorizationCheck(HttpSession session) {
        Member LoginMember = getLoggedInMember(session);
        String role = LoginMember.getRole(); //admin, member
        if (role.equals("ADMIN")) {
            return true;
        } else {
            return false;
        }
    }

}