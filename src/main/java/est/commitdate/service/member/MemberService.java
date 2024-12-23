package est.commitdate.service.member;

import est.commitdate.dto.member.MemberProfileRequest;
import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.exception.*;
import est.commitdate.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
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

        // MemberSignUpRequest 에서 엔티티 생성
        Member member = request.toEntity(encryptedPassword);

        memberRepository.save(member);
        System.out.println(" 회원가입 완료! : " + member.toString());
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
        String text = "임시비밀번호 : " + tempPassword;
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




}