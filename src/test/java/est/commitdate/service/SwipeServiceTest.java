package est.commitdate.service;

import est.commitdate.dto.board.BoardDto;
import est.commitdate.dto.like.LikeDto;
import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.dto.post.PostDto;
import est.commitdate.entity.Board;
import est.commitdate.entity.Like;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.exception.BoardNotFoundException;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.LikeRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;

@Slf4j
@SpringBootTest
class SwipeServiceTest {

    @Autowired
    private SwipeService swipeService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;


    @BeforeAll
    static void setUp(
            @Autowired BoardRepository boardRepository,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired MemberRepository memberRepository,
            @Autowired PostRepository postRepository){

        MemberSignUpRequest requestDto;
        PostDto postDto;
        BoardDto boardDto;
        Board findBoard;
        Member findMember;

        //Board 저장
        boardDto = BoardDto.builder()
                .boardName("SwipeMainPage")
                .build();
        boardRepository.save(Board.of(boardDto));

        for (int i = 0; i < 20; i++) {
            //User 20명 저장
            requestDto  = new MemberSignUpRequest(
                    "testuser@test.com" + i,
                    "testUser",
                    "password123",
                    "tester" + i,
                    "01012345678" + i
            );
            String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
            Member member = requestDto.toEntity(encryptedPassword);
            memberRepository.save(member);
        }

        for (int i = 1; i < 1000 ; i++) {


            //보드 찾기
            findBoard = boardRepository.findByBoardId(1)
                    .orElseThrow(BoardNotFoundException::new);

            //임의의 사용자 찾기.
            Random r = new Random(System.currentTimeMillis());
            findMember = memberRepository.findById(r.nextLong(19)+1)
                        .orElseThrow(EntityNotFoundException::new);

            //Post20개 저장
            postDto = PostDto.builder()
//                    .boardId(findBoard.getBoardId())
//                    .author(findMember.getNickname())
                    .title("Random title" + i)
                    .text("Random text" + i)
                    .description("Random description" + i)
                    .likeCount(0)
                    .build();

            postRepository.save(Post.testTransformEntity(postDto,findBoard,findMember));
        }
    }


    @Test
    @DisplayName("Make Like Test")
    void likeTest() throws Exception{

        MemberSignUpRequest requestDto;
        PostDto postDto;
        BoardDto boardDto;
        Board findBoard;
        Member findMember;
        Post findPost;

        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < 300; i++) {
        findMember = memberRepository.findById(r.nextLong(499)+1)
                .orElseThrow(EntityNotFoundException::new);

        findPost = postRepository.findByPostId(r.nextLong(499)+1)
                .orElseThrow(EntityNotFoundException::new);


        Like likeEntity = Like.of(findMember,findPost);

        likeRepository.save(likeEntity);


            assertThat(likeEntity.getMember()).isEqualTo(findMember);
            assertThat(likeEntity.getPost()).isEqualTo(findPost);

        }
    }

    @Test
    @DisplayName("Like Test")
    void likeDtoTest() throws Exception{

        Random r = new Random(System.currentTimeMillis());

        Like like = likeRepository.findByLikeId(r.nextLong(499)+1).orElseThrow(EntityNotFoundException::new);

    }


}

