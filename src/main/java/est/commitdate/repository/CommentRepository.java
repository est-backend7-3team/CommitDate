package est.commitdate.repository;

import est.commitdate.entity.Comment;
import est.commitdate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByPost(Post post);
}
