package est.commitdate.service;

import est.commitdate.dto.MemberAdditionalInfo;
import est.commitdate.dto.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.exception.*;
import est.commitdate.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signUp(MemberSignUpRequest request) {

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encryptedPassword);

        Member member = request.toEntity();

        memberRepository.save(member);
    }

    @Transactional
    public void oauthSignup(MemberAdditionalInfo memberAdditionalInfo) {
        Member member = Member.of(memberAdditionalInfo); // cera
        memberRepository.save(member);
    }

    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(
                        ()-> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다.")
                );
    }



    protected void validateDuplicateUser(MemberSignUpRequest request) {
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
    }


}