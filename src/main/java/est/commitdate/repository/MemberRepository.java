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



    // 삭제된 데이터만 조회
    @Query(value = "SELECT * FROM Member  WHERE status = 0", nativeQuery = true)
    List<Member> findDeletedMembers();

    // 삭제된 데이터 전부를 포함하여 조회
    @Query(value = "SELECT * FROM Member", nativeQuery = true)
    Optional<Member> findAllWithDeletedMembers();

    // 특정 삭제된 데이터만 조회 -> :바인딩할필드이름 jpa에서 sql문에 :를 사용하면 필드와 바인딩 됨
    @Query(value = "SELECT * FROM Member WHERE member_id = :id AND status = 0", nativeQuery = true)
    Optional<Member> findDeleteById(Integer id);

}