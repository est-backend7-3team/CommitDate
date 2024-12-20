package est.commitdate.repository;

import est.commitdate.entity.Board;
import est.commitdate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    Optional<Member> findById(Long id);
}
