package est.commitdate.service.memberService;

import est.commitdate.dto.memberDto.MemberSignUpRequest;
import est.commitdate.entity.memberEntity.Member;
import est.commitdate.exception.DuplicatedEmailException;
import est.commitdate.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpRequest request) {
        // 이메일 중복 여부 확인
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicatedEmailException("이미가입된 이메일 입니다.");
        }
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encryptedPassword)
                .role("USER") // 기본값은 회원
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1) // 기본값 1
                .build();

        memberRepository.save(member);
    }

}