package est.commitdate.controller;


import est.commitdate.dto.post.PostDto;
import est.commitdate.dto.post.PostUpdateDto;
import est.commitdate.service.BoardService;
import est.commitdate.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final BoardService boardService;

    @GetMapping("")
    public String postView(Model model) {
        model.addAttribute("post", postService.PostList());
        model.addAttribute("boards", boardService.BoardList());
        return "/view/post/redit";
    }

    @GetMapping("/saveView")
    public String postSaveView(Model model) {
        model.addAttribute("post", new PostDto());
        model.addAttribute("boards", boardService.BoardList());
        return "/view/post/postSave";
    }

    @PostMapping("/save")
    public String postSavePost(PostDto postDto) {
        postService.save(postDto);
        return "redirect:/post";
    }

    @GetMapping("/updateView/{id}")
    public String postUpdateView(@PathVariable Long id, Model model) {
        model.addAttribute("post" , PostDto.from(postService.getPostById(id)));
        return "/view/post/postUpdate";
    }

    @PostMapping("/update")
    public String postUpdatePost(PostUpdateDto postDto) {
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
