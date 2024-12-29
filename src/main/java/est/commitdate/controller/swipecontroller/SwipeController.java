package est.commitdate.controller.swipecontroller;

import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.service.ChatService;
import est.commitdate.service.SwipeService;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/swipe")
public class SwipeController {

    private final SwipeService swipeService;
    private final MemberService memberService;


    @GetMapping("")
    public String getSwipePage() {
        return "view/swipe";
    }


    @ResponseBody
    @GetMapping("/jsons")
    public ResponseEntity<SwipeDto> getSwipeJson(HttpSession session) {

        Member user = memberService.getLoggedInMember(session);
        Post randomPost = swipeService.getRandomPost(2);

        SwipeDto swipeDto = SwipeDto.from(randomPost);

        //손님이 아니라면 해당 포스트 좋아요 한 기록찾기
        if(user != null) {
            swipeDto.setIsLike(swipeService.isLike(user , randomPost));
            swipeDto.setIsBlocked(swipeService.isBlocked(user , randomPost));
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
        }else{
            return ResponseEntity.status(401).body("AccessDenied.");
        }
    }

    @ResponseBody
    @PostMapping("/api/blockPost")
    public ResponseEntity<String> blockPost(@RequestBody Map<String,Object> payload, HttpSession session) {

        String result = swipeService.blockPost(payload, session);

        log.info("result = {}", result);

        if(result.equals("IgnoreSuccess")){
            return ResponseEntity.ok("Success");
//        }else if(result.equals("CancelIgnore")) {
//            return ResponseEntity.ok("CancelSuccess");
        }else {
            return ResponseEntity.status(401).body("AccessDenied");
        }

    }
}


