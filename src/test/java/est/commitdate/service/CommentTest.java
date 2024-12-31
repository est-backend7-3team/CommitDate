package est.commitdate.service;


import est.commitdate.controller.PostCommentController;
import est.commitdate.dto.board.BoardDto;
import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.dto.post.PostDto;
import est.commitdate.entity.Comment;
import est.commitdate.service.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class CommentTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    private BoardDto savedBoard;

    @BeforeEach
    void setUp() {
        BoardDto saveReq = BoardDto.builder().boardName("자유게시판").build();
        savedBoard = boardService.save(saveReq);

    }

    @Test
    @DisplayName("댓글테스트")

    void 댓글테스트() throws Exception {
        MemberSignUpRequest savedMember=MemberSignUpRequest.builder()
                .email("test@gmail.com")
                .username("PostWriter")
                .password("12345678")
                .nickname("11")
                .phoneNumber("010-5398-4755")
                .build();
        memberService.signUp(savedMember);

        MemberSignUpRequest savedMember2=MemberSignUpRequest.builder()
                .email("test2@gmail.com")
                .username("CommentWriter")
                .password("12345678")
                .nickname("CommentWriter")
                .phoneNumber("010-5398-8888")
                .build();
        memberService.signUp(savedMember2);

        PostDto postSaveReq = PostDto.builder()
                .boardId(savedBoard.getBoardId())
                .title("1번 게시판의 게시글")
                .description("1번째 게시글 입니다.")
                .author("11")
                .build();
        PostDto savedPost = postService.save(postSaveReq);
        log.info(savedPost.toString());
        Comment comment = commentService.save(postService.getPostById(savedPost.getPostId()), memberService.getMemberByNickname("CommentWriter"), "댓글내용입니다.");
        log.info(comment.getContent()+ " " + comment.getMember().getNickname() + " " +comment.getPost().getPostId());
        Comment findComment = commentService.getCommentByPost(postService.getPostById(savedPost.getPostId()));

        assertThat(findComment.getContent()).isEqualTo(comment.getContent());

    }
}
