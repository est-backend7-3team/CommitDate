package est.commitdate.controller.swipecontroller;

import est.commitdate.dto.like.LikeDto;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.service.ChooseService;
import est.commitdate.service.SwipeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/choose")
public class ChooseController {

    private final ChooseService chooseService;
    private final SwipeService swipeService;

    @GetMapping("/{id}")//최초들어오는 부분
    public String getChoicePage(Model model, @PathVariable String id, HttpSession session) {
        if ("null".equals(id) || id == null || id.isBlank()) {
            return null;
        }
        log.info("Received id: {}",id);
        model.addAttribute("id", Long.valueOf(id));
        return "view/choose";
    }

    @GetMapping("/likes/{id}")//최초들어오는 부분
    public String getLikesPage(Model model, @PathVariable String id, HttpSession session) {
        if ("null".equals(id) || id == null || id.isBlank()) {
            return null;
        }
        log.info("Received MemberId: {}",id);
        model.addAttribute("id", Long.valueOf(id));
        return "view/likeList";
    }

    @ResponseBody
    @PostMapping("/Jsons")
    public List<ChooseDto> getJsons(@RequestBody Map<String,Object> payload, HttpSession session) {
        return chooseService.getChooseDtos(payload, session);
    }

    @ResponseBody
    @PostMapping("/likeJsons")
    public List<LikeDto> getLikeJsons(@RequestBody Map<String,Object> payload, HttpSession session) {
        return chooseService.getLikeDtos(payload, session);
    }
}

