package est.commitdate.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String home() {
        return "view/swipe";
    }

    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "view/aboutUs";
    }
}
