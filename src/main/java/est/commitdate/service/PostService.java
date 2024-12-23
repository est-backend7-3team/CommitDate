package est.commitdate.service;

import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.entity.Board;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.exception.PostNotFoundException;
import est.commitdate.repository.PostRepository;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    public PostDto save(PostDto postDto) {
        Board findBoard = boardService.getBoardById(postDto.getBoardId());

        Member findMember = memberService.getMemberByNickname(postDto.getAuthor());
        log.info("찾은사람" + findMember.getNickname());
        return PostDto.from(postRepository.save(Post.of(postDto, findBoard, findMember))) ;
    }

    // 현재 로그인 되어있는 사용자와 post의 작성자가 같아함 혹은 관리자
    public void update(PostUpdateDto postUpdateDto) {
        Post findPost = getPostById(postUpdateDto.getPostId());
        findPost.update(postUpdateDto);
    }
    // 현재 로그인 되어있는 사용자와 post의 작성자가 같아함 혹은 관리자
    public void delete(Long id) {
        Post findPost = getPostById(id);
        findPost.delete();
    }

    public void restore(Long id) {
        Post findPost = getDeletePostById(id);
        findPost.restore();
    }

    public Post getPostById(Long postId) {
        return postRepository.findByPostId(postId)
                .orElseThrow(
                        ()-> new PostNotFoundException("해당 게시글을 찾을 수 없습니다.")
                );
    }
    public Post getDeletePostById(Long postId) {
        return postRepository.findDeleteById(postId)
                .orElseThrow(
                        ()->new PostNotFoundException("삭제된 해당 게시글을 찾을 수 없습니다.")
                );
    }

    // 모든 게시판의 글들을 불러오기
    public List<PostDto> PostList() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            PostDto findDTO = PostDto.from(post);
            postDtos.add(findDTO);
        }
        return postDtos;
    }
    // 게시판에 해당되는 글들만 불러오기
    public List<PostDto> getPostsByBoardId(Integer boardId) {
        log.info("boardId = " + boardId);
        Board findBoard = boardService.getBoardById(boardId);
        List<Post> BoardPosts = postRepository.findByBoard(findBoard);
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : BoardPosts) {
            PostDto findDTO = PostDto.from(post);
            postDtos.add(findDTO);
        }
        return postDtos;
    }

    // post의 작성자를 찾고 현재 로그인 되어있는 사용자와 비교하여 일치하면 true
    public Boolean postAuthorizationCheck(Long id, HttpSession session) {
        String PostWriter = getPostById(id).getMember().getNickname();
        String loginMemberNickname = memberService.getLoggedInMember(session).getNickname();
        if(PostWriter.equals(loginMemberNickname)) {
            return true;
        } else {
            return false;
        }
    }




}
