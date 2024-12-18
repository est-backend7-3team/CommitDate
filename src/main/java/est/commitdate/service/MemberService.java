package est.commitdate.service;

import est.commitdate.dto.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.exception.DuplicatedEmailException;
import est.commitdate.exception.DuplicatedNicknameException;
import est.commitdate.exception.DuplicatedPhoneNumberException;
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

        Member member = Member.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encryptedPassword)
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .role("USER") // 기본값은 회원
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .additionalInfoCompleted(true)
                .status(1) // 기본값 1
                .build();

        memberRepository.save(member);
        System.out.println(" 회원가입 완료! : " + member.toString());
    }

}