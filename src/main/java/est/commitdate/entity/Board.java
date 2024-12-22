package est.commitdate.entity;



import est.commitdate.dto.board.BoardDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.util.List;

// @Where()을 사용하면 모든 쿼리에 where이 붙어서 삭제가 안된 데이터만 조회가 가능하지만 삭제된 데이터를 복구하거나 접근할 때 접근할 수 없는 문제 발생
// @Where ->@SQLRestriction 로 대체됨
// 이에 대한 해결책으로
// 1. @Query를 직접 Repository에 만들어서 삭제된 데이터도 조회할 수 있도록 하는 방식이 있다.->  @Query("SELECT b FROM Board b WHERE b.status = 0") // 삭제된 데이터 조회
// 2. Hibernate의 Filter 객체 사용

@Getter
@Entity
@Table(name = "Board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Board Set status = 0 WHERE board_id = ?")
@SQLRestriction("status = 1")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @Column(name = "boardname", nullable = false)
    private String boardName;

    @Column(name = "status", nullable = false)
    private int status = 1;

    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL)
    private List<Post> posts;

    // dto를 entity로 변환
    public static Board of(BoardDto dto) {
        Board board = new Board();
        board.boardName = dto.getBoardName();
        return board;
    }
    public void update(BoardDto dto) {
        this.boardName = dto.getBoardName();
    }

    public void delete() {
        this.status = 0;
        if(posts != null) {
            for (Post post : posts) {
                post.delete();
            }
        }
    }
    public void restore() {
        this.status = 1;
    }


    @Builder
    private Board(Integer boardId, int status, String boardName) {
        this.boardId = boardId;
        this.status = status;
        this.boardName = boardName;
    }
}




