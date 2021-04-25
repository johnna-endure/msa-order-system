package spring.msa.uiserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "/member/register";
    }

}
