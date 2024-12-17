package est.commitdate.dto;


import est.commitdate.entity.Board;
import lombok.*;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardDto {
    
    private Integer boardId;
    private String boardName;
    private int status;

    //entity를 dto로 변환
    public static BoardDto from(Board board) {
        return new BoardDto(board.getBoardId(),board.getBoardName(), board.getStatus());
    }
}
