package est.commitdate.service.member;

import est.commitdate.dto.member.FormUserDetails;
import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import est.commitdate.entity.Member;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username+"은 회원이 아닙니다."));

        return FormUserDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .id(member.getId())
                .build();
    }

}
