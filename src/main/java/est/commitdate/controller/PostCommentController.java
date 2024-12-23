package est.commitdate.controller;


import est.commitdate.dto.comment.CommentUpdateDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.entity.Comment;
import est.commitdate.entity.Member;
import est.commitdate.service.CommentService;
import est.commitdate.service.PostService;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostCommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/comment/{id}")
    public String postComment(String content, HttpSession session, @PathVariable("id") Long id) {

        Member writer = memberService.getLoggedInMember(session);
        if (writer == null) {
            return "redirect:/login";  // 로그인 페이지 URL로 리다이렉션
        }
        commentService.save(postService.getPostById(id), writer, content);
        return "redirect:/post/view/"+id;
    }

    @PostMapping("/comment/update")
    public String CommentUpdate(PostUpdateDto dto) {
        return "redirect:/post";

    }

    @PostMapping("/comment/{postId}/delete/{id}")
    public String deleteComment(@PathVariable Long postId,@PathVariable Long id) {
        commentService.delete(id);
        return "redirect:/post/view/"+postId;
    }

}
