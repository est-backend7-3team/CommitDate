package est.commitdate.service;

import est.commitdate.dto.board.BoardDto;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class SwipeServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LikeRepository likeRepository;



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

            //보드 찾기
            findBoard = boardRepository.findByBoardId(1)
                    .orElseThrow(BoardNotFoundException::new);

        for (int i = 1; i < 500 ; i++) {

            //임의의 사용자 찾기.
            Random r = new Random(System.currentTimeMillis());
            findMember = memberRepository.findById(r.nextLong(19)+1)
                        .orElseThrow(EntityNotFoundException::new);

            //Post1000개 저장
            postDto = PostDto.builder()
//                    .boardId(findBoard.getBoardId())
//                    .author(findMember.getNickname())
                    .title("Random title" + i)
                    .text("Random text" + i)
                    .description("Random description" + i)
                    .build();

            postRepository.save(Post.testTransformEntity(postDto,findBoard,findMember));
        }
    }


    @Test
    @DisplayName("Make Like Test")
    void likeTest() throws Exception{

        Member findMember;
        Post findPost;

        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < 300; i++) {

        findMember = memberRepository.findById(r.nextLong(19)+1)
                .orElseThrow(EntityNotFoundException::new);

        findPost = postRepository.findByPostId(r.nextLong(499)+1)
                .orElseThrow(EntityNotFoundException::new);


        Like likeEntity = Like.of(findMember,findPost);

        likeRepository.save(likeEntity);


            assertThat(likeEntity.getMember()).isEqualTo(findMember);
            assertThat(likeEntity.getPost()).isEqualTo(findPost);

        }
    }

//    @Test
//    @DisplayName("Make to Target User Like")
//    void makeLike() throws Exception {
//
//        Member findMember;
//        Post findPost = postRepository.findByPostId(500L) //내가 쓴 게시물 id = 500번.
//                .orElseThrow(EntityNotFoundException::new);
//
//        Random r = new Random(System.currentTimeMillis());
//
//
//        for (int i = 0; i < 300; i++) {
//            findMember = memberRepository.findById(r.nextLong(19) + 1)
//                    .orElseThrow(EntityNotFoundException::new);
//
//        }
//
//    }
}

