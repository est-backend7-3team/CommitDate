package est.commitdate.repository;

import est.commitdate.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByLikeId(Long likeId);

}
