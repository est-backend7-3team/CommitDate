package est.commitdate.controller.member;

import est.commitdate.entity.Member;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private MemberService memberService;

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        Member member = memberService.getLoggedInMember(session);
        if (member != null) {
            model.addAttribute("member", member);
        }
    }
}