package est.commitdate.service;

import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Board;
import est.commitdate.entity.Post;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SwipeService {


    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    private final MemberService memberService;
    private final BoardService boardService;
    private final PostService postService;


    public List<ChooseDto> getDummyChooseDTO(int volume){
        List<ChooseDto> chooseDTOList = new ArrayList<>();
        for (int i = 0; i < volume; i++) {
            chooseDTOList.add(ChooseDto.builder()
                                .userName("username"+i)
                                .comment("comment"+i)
                                .profileImageURL("{dummy URL}")
                                .timestamp(1)
                                .build()
            );
        }
        return chooseDTOList;
    }


}
