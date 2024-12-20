package est.commitdate.controller.swipecontroller;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Post;
import est.commitdate.repository.PostRepository;
import est.commitdate.service.SwipeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/swipe")
public class SwipeController {

    private final SwipeService swipeService;
    private final PostRepository postRepository;



    @GetMapping("")
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

        List<ChooseDto> swipeList = swipeService.getDummyChooseDTO(2);

        log.info("tmpList = {}", swipeList.toArray().length);

        model.addAttribute("swipeList", swipeList);

        log.info("chooseDTOList[0] = {}", swipeList.getFirst());

        return "view/choose";
    }

    @ResponseBody
    @GetMapping("/jsons")
    public ResponseEntity<SwipeDto> getSwipeJson() {

        Random r = new Random(System.currentTimeMillis());

        Post findPost = postRepository.findByPostId(r.nextLong(59)+1).orElseThrow(
                () -> new EntityNotFoundException("Post not found")
        );
        SwipeDto swipeDto = SwipeDto.from(findPost);

        return ResponseEntity.ok(swipeDto);
    }

    @ResponseBody
    @PostMapping("/api/like")
    public ResponseEntity<String> sendLike(@RequestBody String postId, HttpSession session) {

        // 세션에서 SPRING_SECURITY_CONTEXT 속성 가져오기
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");


        log.info("postId = {}", postId);

        if(securityContext != null){
            CustomUserDetails UserDetails = (CustomUserDetails) securityContext.getAuthentication().getPrincipal();
            log.info("UserDetails.getEmail() = {}", UserDetails.getEmail());
        }

//        if (authentication == null) {
//            log.info("user is null");
//            return ResponseEntity.status(401).body("로그인이 필요합니다.");
//        }



        return ResponseEntity.ok("test Ok");

    }
}


