package est.commitdate.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberLogInController {

    // 로그인 페이지 반환
    @GetMapping("/login")
    public String loginPage() {
        return "view/login";
    }

}
