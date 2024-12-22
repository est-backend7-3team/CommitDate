package est.commitdate.repository;

import est.commitdate.entity.Ignore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IgnoreRepository extends JpaRepository<Ignore,Long> {

    Optional<Ignore> findByIgnoreId(Long ignoreId);

    Optional<Ignore> findByMemberIdAndPostPostId(Long memberId, Long postId);

    //필요없을거같기도하고...
    @Query(value = "SELECT * FROM post_ignore WHERE member_id = :memberId AND post_id = :postId AND status = 0", nativeQuery = true)
    Optional<Ignore> findByMemberIdAndPostPostIdAndStatus(Long memberId, Long postId);

}

