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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final MemberService memberService;

    // 게시글 목록 페이지
    @GetMapping("")
    public String postListView(Model model, HttpSession session) {
        Map<String, String> loginInfo = memberService.getLoginInfo(session);
        model.addAttribute("nickname", loginInfo.get("nickname"));
        model.addAttribute("role", loginInfo.get("role"));
        model.addAttribute("post", postService.PostList());
        model.addAttribute("boards", boardService.BoardList());
        model.addAttribute("pageTitle", "게시글 목록");

        // 경로 수정
        return "view/post/posts";
    }

    // 게시글 상세 페이지
    @GetMapping("/view/{id}")
    public String postDetailView(@PathVariable Long id, Model model, HttpSession session) {
        Map<String, String> loginInfo = memberService.getLoginInfo(session);
        model.addAttribute("nickname", loginInfo.get("nickname"));
        model.addAttribute("role", loginInfo.get("role"));
        PostDetailDto post = PostDetailDto.from(postService.getPostById(id));
        model.addAttribute("postDetail", post);

        // 경로 수정
        return "view/post/postDetail";
    }

    // 게시글 생성 페이지
    @GetMapping("/saveView")
    public String postSaveView(Model model) {
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("boards", boardService.BoardList());

        return "view/post/postSave";
    }

    // 게시글 저장 요청
    @PostMapping("/save")
    public String postSavePost(PostDto postDto, HttpSession session) {
        Member loggedInMember = memberService.getLoggedInMember(session);
        postDto.setAuthor(loggedInMember.getNickname());
        postService.save(postDto);

        return "redirect:/post";
    }

    // 게시글 수정 페이지
    @GetMapping("/updateView/{id}")
    public String postUpdateView(@PathVariable Long id, Model model, HttpSession session) {
        if (postService.postAuthorizationCheck(id, session) || memberService.AuthorizationCheck(session)) {
            model.addAttribute("postUpdateDto", PostUpdateDto.from(postService.getPostById(id)));
            return "view/post/postUpdate";
        }
        return "redirect:/post";
    }

    // 게시글 삭제 요청
    @PostMapping("/delete/{id}")
    public String postDeletePost(@PathVariable Long id, HttpSession session) {
        if (postService.postAuthorizationCheck(id, session) || memberService.AuthorizationCheck(session)) {
            postService.delete(id);
        }
        return "redirect:/post";
    }
    @ResponseBody
    @PostMapping("/api/commentDelete")
    public ResponseEntity<String> postCommentDelete(@RequestBody Map<String,Object> removeJson, HttpSession session) {

        return ResponseEntity.ok(postService.postCommentRemove(removeJson, session));

    }

    @ResponseBody
    @PostMapping("/api/commentEdit")
    public ResponseEntity<String> postCommentEdit(@RequestBody Map<String,Object> removeJson, HttpSession session) {

        return ResponseEntity.ok(postService.postCommentEdit(removeJson, session));

    }


}


