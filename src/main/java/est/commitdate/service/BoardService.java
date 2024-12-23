package est.commitdate.service;


import est.commitdate.dto.board.BoardDto;
import est.commitdate.entity.Board;
import est.commitdate.exception.BoardNotFoundException;
import est.commitdate.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;


    // 컨트롤러로 돌려주는 dto 값이 return type , 유저로 부터 받는 값이 인자값
    public BoardDto save(BoardDto boardDto) {
        //저장하는 것 뿐만 아니라 저장하고 dto로 반환하기
        return BoardDto.from(boardRepository.save(Board.of(boardDto)));
    }

    public void update(BoardDto boardDto) {
        Board findBoard = getBoardById(boardDto.getBoardId());
        // jpa의 변경감지에 의해 boardRepository.save를 안해도 자동으로 변경된 값이 저장됨
        findBoard.update(boardDto);
    }
    @Transactional
    public void delete(Integer id) {
        Board findBoard = getBoardById(id);
        findBoard.delete();
        System.out.println("완료 변경됨" + findBoard.getStatus()+ " " + findBoard.getBoardName());

    }
    public void restore(Integer id) {
        Board findBoard = getDeleteBoardById(id);
        findBoard.restore();
    }


    //제목으로 board를 찾고 해당 제목의 글이 없다면 오류 발생
    public Board getBoardById(Integer boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(
                        ()-> new BoardNotFoundException("해당 게시판을 찾을 수 없습니다.")
                );
    }

    public Board getDeleteBoardById(Integer boardId) {
        return boardRepository.findDeleteById(boardId)
                .orElseThrow(
                        ()-> new BoardNotFoundException("삭제된 해당 게시판을 찾을 수 없습니다.")
                );
    }

    //사용할 boardDto 목록 불러오기
    public List<BoardDto> BoardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto findDTO = BoardDto.from(board);
            boardDtos.add(findDTO);
        }
        return boardDtos;
    }
    // 삭제된 게시판 불러오기
    public List<BoardDto> BoardDeleteList() {
        List<Board> boards = boardRepository.findDeletedBoards();
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto findDTO = BoardDto.from(board);
            boardDtos.add(findDTO);
        }
        return boardDtos;
    }



}