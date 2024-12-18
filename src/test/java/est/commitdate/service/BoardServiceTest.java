package est.commitdate.service;

import est.commitdate.dto.board.BoardDto;
import est.commitdate.entity.Board;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
class BoardServiceTest {

    @Autowired
    public BoardService boardService;

    // 관리자만 생성 삭제 수정할 수 있도록변경
    @Test
    @DisplayName("게시판 저장테스트")
    // @Rollback(value = false) 이걸 사용하면 메모리에 남겨짐
    void 게시판생성() throws Exception {
        // 요청만 사용자로 부터 입력 받는값
        BoardDto saveReq = new BoardDto(0,"title", 0);
        assertThat(saveReq.getBoardId()).isEqualTo(0);

        BoardDto saved = boardService.save(saveReq);
        assertThat(saved.getBoardId()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시판이름변경")
    void 게시판이름변경() throws Exception {

        BoardDto saveReq = new BoardDto(0,"title", 0);
        BoardDto saved = boardService.save(saveReq);
        BoardDto updateReq = new BoardDto(saved.getBoardId(),"update title", 0);

        boardService.update(updateReq);
        assertThat(saved.getBoardId()).isEqualTo(updateReq.getBoardId());
        assertThat(saved.getBoardName()).isNotEqualTo(updateReq.getBoardName());
    }

    @Test
    @DisplayName("게시판삭제, 복구")
    @Transactional
    void 게시판삭제및복구() throws Exception {
        BoardDto saveReq = new BoardDto(0,"title", 0);
        BoardDto saved = boardService.save(saveReq);
        Board boardById = boardService.getBoardById(saved.getBoardId());
;
        boardService.delete(saved.getBoardId());
        assertThat(boardById.getStatus()).isEqualTo(0);
        boardService.restore(saved.getBoardId());
        assertThat(boardById.getStatus()).isEqualTo(1);
    }


}