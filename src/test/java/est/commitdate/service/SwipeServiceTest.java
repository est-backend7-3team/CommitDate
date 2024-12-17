package est.commitdate.service;

import est.commitdate.entity.Board;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class SwipeServiceTest {


    @Autowired
    private SwipeService swipeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;


    @BeforeEach
    void setUp() {
        //기존 내용 삭제
//        postRepository.deleteAll();
//        boardRepository.deleteAll();
//        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("SwipeTest")
    void SwipeTest() throws Exception{

    // given

        //Member 엔티티 생성
        Member member = Member.builder()
                .password("1234")
                .email("test@gmail.com")
                .nickname("test")
                .username("사람이름")
                .phoneNumber("010-5261-7904")
                .role("MEMBER")
                .profileImage(null)
                .introduce("안녕하세요라리 진짜 이거 개쩔지 않냐 크흐 ;;;   ㅗㅁㄴ아러ㅣ;자더ㅏㅣ;렂;디ㅓㄹ")
                .comment("쌔끈빠끈한 제 키보드좀 보세요")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1)
                .build();

        //member 저장
        memberRepository.save(member);

        //Board 엔티티 생성
        Board board = Board.builder()
                .boardName("스와이프")
                .status(1)
                .build();

        boardRepository.save(board);

        //Post 엔티티 생성
        Post tmpPost = Post.builder()
                .board(board)
                .title("허허허 좋십니더..")
                .text("저랑 같이 Hello World? 하실분?")
                .member(member)
                .likeCount(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1)
                .build();

        postRepository.save(tmpPost);
        postRepository.flush();

    // when
        Post findPost = postRepository.findByPostId(tmpPost.getPostId()).orElseThrow(() -> new EntityNotFoundException("Post not found") );
        swipeService.postToSwipeDto(findPost);


    // then
        assertThat(postRepository.findAll()).hasSize(1);
        log.info("postRepository.findAll() = {}", findPost);
    }

}