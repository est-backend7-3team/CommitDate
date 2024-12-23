package est.commitdate.controller;


import est.commitdate.dto.post.PostDetailDto;
import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.entity.Member;
import est.commitdate.service.BoardService;
import est.commitdate.service.PostService;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final BoardService boardService;
    private final MemberService memberService;
    // 게시글 자세히 보기
    @GetMapping("/view/{id}")
    public String postDetailView(@PathVariable Long id, Model model) {
        PostDetailDto post = PostDetailDto.from(postService.getPostById(id));
        model.addAttribute("postDetail" , PostDetailDto.from(postService.getPostById(id)));
        return "view/post/fragment/postDetail";
    }
    // 전체 게시판 보기
    @GetMapping("")
    public String postView(Model model) {
        model.addAttribute("post", postService.PostList());
        model.addAttribute("boards", boardService.BoardList());
        model.addAttribute("pageTitle", "Fusion Platform - Main Page");
        return "view/post/base";
    }
    // 해당하는 게시판의 게시글 목록 부르기
    @GetMapping("/{id}" )
    public String postByBoard(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postService.getPostsByBoardId(id));
        model.addAttribute("boards", boardService.BoardList());
        model.addAttribute("pageTitle", "Fusion Platform - Main Page");
        return "view/post/base";
    }
    //게시글 생성창
    @GetMapping("/saveView")
    public String postSaveView(Model model) {
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("boards", boardService.BoardList());
        return "view/post/postSave";
    }
    // 게시글 저장요칭
    @PostMapping("/save")
    public String postSavePost(PostDto postDto, HttpSession session) {
        Member LoginMember = memberService.getLoggedInMember(session);
        postDto.setAuthor(LoginMember.getNickname());
        postService.save(postDto);
        return "redirect:/post";
    }
    // 게시글 업데이트창
    @GetMapping("/updateView/{id}")
    public String postUpdateView(@PathVariable Long id, Model model, HttpSession session) {
        if(postService.postAuthorizationCheck(id,session) || memberService.AuthorizationCheck(session)) {
            model.addAttribute("postUpdateDto" , PostUpdateDto.from(postService.getPostById(id)));
            return "view/post/postUpdate";
        } else {
            return "redirect:/post";
        }

    }
    // 게시글 업데이트 요청
    @PostMapping("/update")
    public String postUpdatePost(PostUpdateDto postDto, HttpSession session) {
        postService.update(postDto);
        return "redirect:/post";
    }
    // 게시글 삭제 요청
    @PostMapping("/delete/{id}")
    public String postDeletePost(@PathVariable Long id , HttpSession session) {
        if(postService.postAuthorizationCheck(id,session) || memberService.AuthorizationCheck(session)) {
            postService.delete(id);
            return "redirect:/post";
        }
        return "redirect:/post";
    }
    // 게시글 복구 요청
    @PostMapping("/restore/{id}")
    public String postRestore(@PathVariable Long id, HttpSession session) {
        if(postService.postAuthorizationCheck(id,session) || memberService.AuthorizationCheck(session)) {
            postService.restore(id);
            return "redirect:/post";
        }
        return "redirect:/post";
    }


}
