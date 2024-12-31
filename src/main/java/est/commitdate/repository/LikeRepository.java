package est.commitdate.repository;

import est.commitdate.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByLikeId(Long likeId);

    Optional<Like> findByMemberIdAndPostPostId(Long memberId, Long postId);

    Optional<Like> findByMemberId(Long memberId);

    @Query(value = "SELECT * FROM post_like WHERE member_id = :memberId AND post_id = :postId AND status = 0", nativeQuery = true)
    Optional<Like> findByMemberIdAndPostPostIdAndStatus(Long memberId, Long postId);

}

