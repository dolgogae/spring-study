package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {
    // 타임리프 관련 추가 설정 방법 사이트
    // https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/appendix-application-properties.html#common-application-properties-templating

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        // 해당 html의 data변수에 값이 들어간다.
        ModelAndView mav = new ModelAndView("/response/hello")
                .addObject("data", "hello!");
        
        return mav;
    }

    /**
     * 리턴값이 view의 논리적 이름이 된다.(templates 밑에 있는 경로)
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello!");        
        return "response/hello";
    }

    /**
     * 권장하지 않는 방법
     * 경로이 이름이랑 url을 맞추는 방법
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model){
        // 반환값이 없으면 mapping된 뷰템플릿으로 간다.
        model.addAttribute("data", "hello!");        
    }
    
}
