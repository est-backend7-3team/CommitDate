package est.commitdate.controller;


import est.commitdate.dto.post.PostDetailDto;
import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.service.BoardService;
import est.commitdate.service.PostService;
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

    @GetMapping("/view/{id}")
    public String postDetailView(@PathVariable Long id, Model model) {
        PostDetailDto post = PostDetailDto.from(postService.getPostById(id));

        model.addAttribute("postDetail" , PostDetailDto.from(postService.getPostById(id)));
        return "view/post/fragment/postDetail";
    }

    @GetMapping("")
    public String postView(Model model) {
        model.addAttribute("post", postService.PostList());
        model.addAttribute("boards", boardService.BoardList());
        model.addAttribute("pageTitle", "Fusion Platform - Main Page");
        return "view/post/base";
    }

    @GetMapping("/{id}" )
    public String postByBoard(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postService.getPostsByBoardId(id));
        model.addAttribute("boards", boardService.BoardList());
        model.addAttribute("pageTitle", "Fusion Platform - Main Page");
        return "view/post/base";
    }

    @GetMapping("/saveView")
    public String postSaveView(Model model) {
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("boards", boardService.BoardList());
        return "view/post/postSave";
    }

    @PostMapping("/save")
    public String postSavePost(PostDto postDto) {
        log.info("저장" + postDto.toString());
        postDto.setAuthor("11"); // 임시방편
        postService.save(postDto);
        return "redirect:/post";
    }

    @GetMapping("/updateView/{id}")
    public String postUpdateView(@PathVariable Long id, Model model) {

        model.addAttribute("postUpdateDto" , PostUpdateDto.from(postService.getPostById(id)));
        return "view/post/postUpdate";
    }

    @PostMapping("/update")
    public String postUpdatePost(PostUpdateDto postDto) {
        log.info("찾은 id : "+postDto);
        postService.update(postDto);
        return "redirect:/post";
    }

    @PostMapping("/delete/{id}")
    public String postDeletePost(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/post";
    }

    @PostMapping("/restore/{id}")
    public String postRestore(@PathVariable Long id) {
        postService.restore(id);
        return "redirect:/post";
    }

}
