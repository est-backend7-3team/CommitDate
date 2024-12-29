package est.commitdate.service;

import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.entity.Board;
import est.commitdate.entity.Comment;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.exception.BoardNotFoundException;
import est.commitdate.exception.PostNotFoundException;
import est.commitdate.repository.CommentRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import est.commitdate.service.member.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;




@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardService boardService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

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
        List<Post> posts = postRepository.findAllPostsOrderedByCreatedAtDesc();
        return posts.stream().map(PostDto::from).toList();
    }

    // 게시판에 해당되는 글들만 불러오기
    public List<PostDto> getPostsByBoardId(Integer boardId) {
        log.info("boardId = " + boardId);
        Board findBoard = boardService.getBoardById(boardId);
        List<Post> BoardPosts = postRepository.findByBoard(findBoard)
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
        return BoardPosts.stream().map(PostDto::from).toList();
    }

    // 유저에 해당되는 글들만 불러오기
    public List<PostDto> getPostsByBoardId(Long userId) {
        log.info("userId = {}", userId);
        Member member = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        List<Post> BoardPosts = member.getPosts()
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
        return BoardPosts.stream().map(PostDto::from).toList();
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

    public String postCommentRemove(@RequestBody Map<String,Object> removeJson, HttpSession session){

        //정보 파싱.
        Long commentId = Long.valueOf((String) removeJson.get("commentId"));

        //DB 조회
        Member LoggedInMember = memberService.getLoggedInMember(session);

        //로그인 안되어있으면 바로 리턴
        if(LoggedInMember == null){
            return "accessDenied_로그인필요";
        }
        log.info("commentId = {}", commentId);
        log.info("LoggedInMember.getEmail() = {}", LoggedInMember.getEmail());
        Member commentWriteMember = commentRepository.findByCommentId(commentId)
                .map(Comment::getMember)
                .orElseThrow(EntityNotFoundException::new);


        log.info("commentWriteMember.getEmail() = {}", commentWriteMember.getEmail());

        // 로그인된 사람과 코멘트를 작성한 사람이 일치하면
        if(LoggedInMember.equals(commentWriteMember)) {
            //해당 코멘트 삭제
            commentRepository.deleteById(commentId);
            return "success";
        }
        return "accessDenied_작성자불일치";
    }

    public String postCommentEdit(@RequestBody Map<String,Object> editJson, HttpSession session){

        //정보 파싱.
        Long commentId = Long.valueOf((String) editJson.get("commentId"));

        //DB 조회
        Member LoggedInMember = memberService.getLoggedInMember(session);

        //로그인 안되어있으면 바로 리턴
        if(LoggedInMember == null){
            return "accessDenied_로그인필요";
        }

        Comment targetComment = commentRepository.findByCommentId(commentId).orElseThrow(EntityNotFoundException::new) ;
        Member commentWriteMember = commentRepository.findByCommentId(commentId)
                .map(Comment::getMember)
                .orElseThrow(EntityNotFoundException::new);

        // 로그인된 사람과 코멘트를 작성한 사람이 일치하면
        if(LoggedInMember.equals(commentWriteMember)) {
            //해당 코멘트 삭제
            targetComment.update(editJson.get("editText").toString());
            return "success";
        }

        return "accessDenied_작성자불일치";
    }

    public List<PostDto> classification(String classification, String id, Member member) {

        List<PostDto> postDtoList = new ArrayList<>();

        try {
            //예외처리
            if (member == null) {
                return null; // 비 로그인일 경우.
            } else if (!(classification.equals("board") || classification.equals("user"))) {
                return null; //classification 이 아니라면
            } else if (classification.equals("user")) {
                if (!member.getId().equals(Long.valueOf(id))) {
                    return null; // 내 게시물에 있는 사람이 세션에 들어있는 사람(동일인)이 아니라면
                }
            }

            //분기처리
            if (classification.equals("board")) { //보드리스트일 때,
                Integer boardId = Integer.valueOf(id);
                postDtoList = getPostsByBoardId(boardId);
            } else { //use r로 들어온경우
                Long userId = Long.valueOf(id);
                postDtoList = getPostsByBoardId(userId);
            }


            return postDtoList;
        }catch (BoardNotFoundException e){
            e.printStackTrace();
            return null; // Exception
        }
    }
}