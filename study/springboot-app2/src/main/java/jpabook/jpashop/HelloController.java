package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    // hello라는 URL이 오면 이 컨트롤러를 호출하겠다.
    // 모델은 Controller에서 데이터를 받아서 View로 넘기는 역할
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!");
        return "hello";
        // 요 리턴이 화면 이름(경로)이 된다.
        // 저 hello에 .html이 자동을 붙음.
        // 따라서 저 결과가 화면으로 이동할 때 hello.html로 자동으로 이동한다.
    }
}
