package est.commitdate.repository;

import est.commitdate.entity.Board;
import est.commitdate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostId(Long id);

    @Query(value = "SELECT * FROM Post WHERE status = 0" ,nativeQuery = true)
    Optional<Post> findDeletedPosts();

    @Query(value = "SELECT * FROM Post" ,nativeQuery = true)
    Optional<Post> findAllWithDeleted();

    // 특정 삭제된 데이터만 조회 -> :바인딩할필드이름 jpa에서 sql문에 :를 사용하면 필드와 바인딩 됨
    @Query(value = "SELECT * from Post where post_id = :id and status = 0", nativeQuery = true)
    Optional<Post> findDeleteById(Long id); // 여기 id가 boardId에 바인딩 됨

}

