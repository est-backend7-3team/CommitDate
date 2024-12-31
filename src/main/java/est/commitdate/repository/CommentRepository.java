package est.commitdate.repository;

import est.commitdate.entity.Board;
import est.commitdate.entity.Comment;
import est.commitdate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByPost(Post post);

    Optional<Comment> findByCommentId(Long commentId);

    // 특정 삭제된 데이터만 조회 -> :바인딩할필드이름 jpa에서 sql문에 :를 사용하면 필드와 바인딩 됨
    @Query(value = "SELECT * FROM comment WHERE comment_Id  = :id AND status = 0", nativeQuery = true)
    Optional<Comment> findDeleteById(Long id);
}
