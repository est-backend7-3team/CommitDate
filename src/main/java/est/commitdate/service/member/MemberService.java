package est.commitdate.service.member;

import est.commitdate.dto.member.MemberProfileRequest;
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
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMailingService memberMailingService;

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
    public void delete(Long id) {
        Member findMember = getMemberById(id);
        findMember.delete();
        System.out.println("탈퇴 완료." + findMember.getStatus()+ " " + findMember.getUsername());
    }

    @Transactional
    public MemberProfileRequest getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다. ID=" + memberId));

        return MemberProfileRequest.fromMember(member);
    }

    @Transactional
    public void updateProfile(Long memberId, MemberProfileRequest form) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다. ID=" + memberId));

        // 새 비밀번호가 입력되었을 때만 암호화
        String encryptedPassword = null;
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            encryptedPassword = passwordEncoder.encode(form.getPassword());
        }

        form.applyToMember(member, encryptedPassword);
    }

    //비밀번호 찾기(임시비밀번호발급)
    public void sendTemporaryPassword(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일이 존재하지 않습니다."));

        // 임시비밀번호 생성
        String tempPassword = generateRandomPassword();

        // 생성된 임시 비밀번호로 해당 계정의 비밀번호 변경
        String encodedPassword = passwordEncoder.encode(tempPassword);
        member.changeTempPassword(encodedPassword);

        // DB에 반영
        memberRepository.save(member);

        //이메일 발송
        String subject = "Commitdate 임시 비밀번호 안내";
        String text = "임시비밀번호 : " + tempPassword + "\n임시비밀번호는 즉시 변경해주시기 바랍니다.";
        memberMailingService.sendMail(email, subject, text);

    }

    //랜덤한 임시비밀번호 발급메서드 (10자리/대소문자+숫자+특수문자)
    private String generateRandomPassword() {

        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
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