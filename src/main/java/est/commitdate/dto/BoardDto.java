package est.commitdate.dto;


import est.commitdate.entity.Board;
import lombok.*;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private String boardName;
    private int status;

    public static BoardDto from(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.boardName = board.getBoardName();
        boardDto.status = board.getStatus();
        return boardDto;
    }
}
