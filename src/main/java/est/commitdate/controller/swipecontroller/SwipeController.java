package est.commitdate.controller.swipecontroller;

import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.service.SwipeService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/swipe")
public class SwipeController {

    private final SwipeService swipeService;


    @GetMapping("")
    public String getSwipePage() {

//        //이용 유저 정보, 랜더링할 Entity Post 객체 준비.
//        Member user = swipeService.getLoggedInMember(session);
//        Post randomPost = swipeService.getRandomPost(499);

//        //View 랜더링 준비
//        SwipeDto swipeDto = SwipeDto.from(randomPost);

//        //손님이 아니라면 해당 포스트 좋아요 한 기록찾기
//        if(user != null) {
//            swipeDto.setIsLike(swipeService.isLike(user , randomPost));
//        }
//
//        log.info("user = {}", user);
//        log.info("swipeDto = {}", swipeDto);
//        model.addAttribute("swipeDTO", swipeDto);



        return "view/swipe";
    }


    @GetMapping("/choose")
    public String getChoicePage(Model model, HttpSession session) {

        List<ChooseDto> swipeList = swipeService.getDummyChooseDTO(2);

        log.info("tmpList = {}", swipeList.toArray().length);

        model.addAttribute("swipeList", swipeList);

        log.info("chooseDTOList[0] = {}", swipeList.getFirst());

        return "view/choose";
    }

    @ResponseBody
    @GetMapping("/jsons")
    public ResponseEntity<SwipeDto> getSwipeJson(HttpSession session) {

        Member user = swipeService.getLoggedInMember(session);
        Post randomPost = swipeService.getRandomPost(499);

        SwipeDto swipeDto = SwipeDto.from(randomPost);

        //손님이 아니라면 해당 포스트 좋아요 한 기록찾기
        if(user != null) {
            swipeDto.setIsLike(swipeService.isLike(user , randomPost));
        }

        log.info("user = {}", user);
        log.info("swipeDto = {}", swipeDto);

        return ResponseEntity.ok(swipeDto);
    }

    @ResponseBody
    @PostMapping("/api/toggleLike")
    public ResponseEntity<String> toggleLike(@RequestBody Map<String,Object> payload, HttpSession session) {

        String result = swipeService.toggleLike(payload, session);

        if(result.equals("CancelSuccess") || result.equals("LikeSuccess")){
            return ResponseEntity.ok("Success");
        }

        log.info("AccessDenied");
        return ResponseEntity.status(401).body("권한 없음.");
    }

}


