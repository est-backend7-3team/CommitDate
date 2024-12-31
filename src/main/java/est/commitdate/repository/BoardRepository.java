package est.commitdate.repository;

import est.commitdate.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardId(Integer boardId);

    // 삭제된 데이터만 조회
    @Query(value = "SELECT * FROM board  WHERE status = 0", nativeQuery = true)
    List<Board> findDeletedBoards();

    // 삭제된 데이터 전부를 포함하여 조회
    @Query(value = "SELECT * FROM board", nativeQuery = true)
    Optional<Board> findAllWithDeleted();

    // 특정 삭제된 데이터만 조회 -> :바인딩할필드이름 jpa에서 sql문에 :를 사용하면 필드와 바인딩 됨
    @Query(value = "SELECT * FROM board WHERE board_id = :id AND status = 0", nativeQuery = true)
    Optional<Board> findDeleteById(Integer id);


}