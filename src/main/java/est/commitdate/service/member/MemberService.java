package est.commitdate.service.member;

import est.commitdate.dto.member.MemberDetailRequest;
import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.exception.*;
import est.commitdate.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
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

        // MemberSignUpRequest 에서 엔티티 생성
        Member member = request.toEntity(encryptedPassword);

        memberRepository.save(member);
        System.out.println(" 회원가입 완료! : " + member.toString());
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



}