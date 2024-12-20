package est.commitdate.service;


import est.commitdate.dto.board.BoardDto;
import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.entity.Post;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;
    private BoardDto savedBoard;

    @BeforeEach
    void setUp() {
        // 게시판 생성
        // BoardDto saveReq = new BoardDto(0,"자유게시판", 0);
        BoardDto saveReq = BoardDto.builder().boardName("자유게시판").build();
        savedBoard = boardService.save(saveReq);

    }

    @Test
    @DisplayName("게시글생성테스트")
    void 게시글생성() throws Exception {
        //멤버 저장로직 넣어서 수정해야함
        PostDto postSaveReq = PostDto.builder()
                .boardId(savedBoard.getBoardId())
                .title("1번 게시판의 게시글")
                .description("1번째 게시글 입니다.")
                .author("작성자")
                .build();

        assertThat(postSaveReq.getPostId()).isEqualTo(null);
        System.out.println("실행" + postSaveReq);
        PostDto savedPost = postService.save(postSaveReq);

        assertThat(savedPost.getPostId()).isEqualTo(1L);

    }


    @Test
    @DisplayName("게시글수정")
    void 게시판수정() throws Exception {
        PostDto postSaveReq = PostDto.builder()
                .boardId(savedBoard.getBoardId())
                .title("1번 게시판의 게시글")
                .description("1번째 게시글 입니다.")
                .build();

        PostDto savedPost = postService.save(postSaveReq);

        PostUpdateDto updateReq = PostUpdateDto.builder()
                .postId(savedPost.getPostId())
                .title("1번 게시글 제목을 수정합니다.")
                .description("1번 게시글 내용을 수정합니다.")
                .likeCount(savedPost.getLikeCount())
                .updatedAt(LocalDateTime.now())
                .build();
        postService.update(updateReq);
        assertThat(savedPost.getPostId()).isEqualTo(updateReq.getPostId());
        assertThat(savedPost.getUpdatedAt()).isNotEqualTo(updateReq.getUpdatedAt());

    }
    //게시글 좋아요

    @Test
    @DisplayName("게시글삭제, 복구")
    @Transactional
    void 게시글삭제및복구() throws Exception {
        PostDto postSaveReq = PostDto.builder()
                .boardId(savedBoard.getBoardId())
                .title("1번 게시판의 게시글")
                .description("1번째 게시글 입니다.")
                .build();
        PostDto savedPost = postService.save(postSaveReq);
        Post findPost = postService.getPostById(savedPost.getPostId());

        postService.delete(savedPost.getPostId());
        assertThat(findPost.getStatus()).isEqualTo(0);
        System.out.println("삭제 " + findPost.getPostId());
        postService.restore(savedPost.getPostId());
        System.out.println("복구 " + findPost);
        assertThat(findPost.getStatus()).isEqualTo(1);


    }


}