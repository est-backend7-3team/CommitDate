package est.commitdate.controller.swipecontroller;

import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Post;
import est.commitdate.repository.PostRepository;
import est.commitdate.service.SwipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/swipe")
public class SwipeController {

    private final SwipeService swipeService;
    private final PostRepository postRepository;


    @GetMapping("/")
    public String getSwipePage(Model model) {

        Random r = new Random(System.currentTimeMillis());

        Post findPost = postRepository.findByPostId(r.nextLong(60)+1).orElseThrow(
                () -> new EntityNotFoundException("Post not found")
        );

        SwipeDto swipeDto = SwipeDto.from(findPost);

        model.addAttribute("swipeDTO", swipeDto);

        return "view/swipe";
    }


    @GetMapping("/choose")
    public String getChoicePage(Model model) {

//        swipeService.testInput(56); //member, board, post 입력

        List<ChooseDto> swipeList = swipeService.getDummyChooseDTO(2);

        log.info("tmpList = {}", swipeList.toArray().length);

        model.addAttribute("swipeList", swipeList);

        log.info("chooseDTOList[0] = {}", swipeList.getFirst());

        return "view/choose";
    }

    @ResponseBody
    @GetMapping("/jsons")
    public ResponseEntity<SwipeDto> getSwipeJson2() {

        Random r = new Random(System.currentTimeMillis());

        Post findPost = postRepository.findByPostId(r.nextLong(59)+1).orElseThrow(
                () -> new EntityNotFoundException("Post not found")
        );
        SwipeDto swipeDto = SwipeDto.from(findPost);

        return ResponseEntity.ok(swipeDto);
    }

}


