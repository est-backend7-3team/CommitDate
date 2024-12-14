package est.commitdate.entity;


import est.commitdate.dto.BoardDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "Board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "boardname", nullable = false)
    private String boardName;

    @Column(name = "status", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int status;

    @OneToMany(mappedBy = "board")
    private List<Post> posts;

    public static Board of(BoardDto dto) {
        Board board = new Board();
        board.boardName = dto.getBoardName();
        return board;
    }


}