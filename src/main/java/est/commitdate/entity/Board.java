package est.commitdate.entity;

import est.commitdate.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Entity
@Table(name = "Board")
@NoArgsConstructor
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
    public Board(Integer boardId, int status, String boardName) {
        this.boardId = boardId;
        this.status = status;
        this.boardName = boardName;
    }
}
//public class Board {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "board_id")
//    private Long boardId;
//
//    @Column(name = "boardname", nullable = false)
//    private String boardName;
//
//    @Column(name = "status", nullable = false)
//    private int status = 1;
//
//    @OneToMany(mappedBy = "board")
//    private List<Post> posts;
//
//    // dto를 entity로 변환
//    public static Board of(BoardDto dto) {
//        Board board = new Board();
//        board.boardName = dto.getBoardName();
//        return board;
//    }
//
//    public void update(BoardDto dto) {
//        this.boardName = dto.getBoardName();
//    }
//
//    @Builder
//    public Board(String boardName, int status) {
//        this.boardName = boardName;
//        this.status = status;
//    }
//
//}