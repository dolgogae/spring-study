package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        // 해당 html의 data변수에 값이 들어간다.
        ModelAndView mav = new ModelAndView("/response/hello")
                .addObject("data", "hello!");
        
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello!");        
        return "response/hello";
    }

    @RequestMapping("/response/hello")
    public void responseViewV3(Model model){
        // 반환값이 없으면 mapping된 뷰템플릿으로 간다.
        model.addAttribute("data", "hello!");        
    }

    
}
