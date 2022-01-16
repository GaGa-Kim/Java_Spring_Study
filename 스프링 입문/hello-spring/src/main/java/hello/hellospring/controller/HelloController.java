package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 어노테이션
public class HelloController {

    @GetMapping("hello") // hello로 접속 시 이 메소드 호출
    public String hello(Model model) {
        // 직접 받았던 hello!! 를 넘겨줌 (key, value)
        model.addAttribute("data", "hello!!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    // 파라미터를 통해 외부에서 입력 받은 것을 넘겨줌
    // 파라미터 정보 단축키 : Ctrl + P
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template.html";
    }

    @GetMapping("hello-string")
    @ResponseBody // http 통신 프로토콜의 응답 body 부분에 데이터를 직접 넣어주겠다는 것
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name; // spring 입력 시, hello spring
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; // 문자가 아닌 객체를 반환하도록
    }

    // static으로 만들 경우 클래스 (HelloController) 안에서 클래스 (HelloController) 를 또 사용 가능
    // java bean 표준 방식 (Getter, Setter) 또는 property 접근 방식이라 함
    static class Hello {
        private String name; // private이므로 Getter, Setter 메소드를 통해서 접근

        // Getter, Setter 단축키 : Alt + Insert
        // java bean que es
        public String getName() { // 꺼낼 때
            return name;
        }

        public void setName(String name) { // 넣을 때
            this.name = name;
        }
    }
}
