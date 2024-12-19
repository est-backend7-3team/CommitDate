package est.commitdate.controller;

import est.commitdate.dto.board.BoardDto;
import est.commitdate.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    public String prefix = "view/board/";

    @GetMapping("")
    public String boardView(Model model) {
        model.addAttribute("boards", boardService.BoardList());
        return prefix+ "board";
    }

    // 게시판 생성 화면
    @GetMapping("/saveView")
    public String boardSaveView(Model model) {
        model.addAttribute("boardDto", new BoardDto());
        return prefix +"boardSave";
    }
    // 생성 요청을 처리
    @PostMapping("/save")
    public String boardSaveReq(BoardDto boardDto) {
        boardService.save(boardDto);
        return "redirect:/board";
    }

    // 게시판 업데이트 화면
    @GetMapping("/updateView/{id}")
    public String boardUpdateView(@PathVariable Integer id, Model model) {
        log.info("updateView 호출 : "+ BoardDto.from(boardService.getBoardById(id)).toString());
        model.addAttribute("boardDto", BoardDto.from(boardService.getBoardById(id)));
        return "view/board/boardUpdate";
    }

    // 게시판 업데이트
    @PostMapping("/update")
    public String boardUpdateReq(BoardDto boardDto) {
        log.info("update요청호출"+ boardDto.toString());
        boardService.update(boardDto);
        return "redirect:/board";
    }
    // 게시판 삭제
    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Integer id) {
        boardService.delete(id);
        return "redirect:/board";
    }

    // 게시판 복구
    @PostMapping("/restore/{id}")
    public String boardRestore(@PathVariable Integer id) {
        boardService.restore(id);
        return "redirect:/board/saveView";
    }
}
