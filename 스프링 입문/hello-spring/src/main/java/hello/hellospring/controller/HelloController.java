package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 어노테이션
public class HelloController {

    @GetMapping("hello") // hello로 접속 시 이 메소드 호출
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }
}
