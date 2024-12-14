package est.commitdate.service;


import est.commitdate.dto.BoardDto;
import est.commitdate.entity.Board;
import est.commitdate.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private BoardRepository boardRepository;


    // 컨트롤러로 돌려주는 dto 값이 return type , 유저로 부터 받는 값이 인자값
    public BoardDto save(BoardDto boardDto) {
        return BoardDto.from(boardRepository.save(Board.of(boardDto)));
    }

    public BoardDto update(BoardDto boardDto) {

    }


}
