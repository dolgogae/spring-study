package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 예전 방식은 컴포넌트의 이름을 가지고 url매핑을 해주었다.
 *
 * 스프링 부트는 InternalResourceViewResolver라는 뷰 리졸버를 자동으로 등록한다.
 * 이때 프로퍼티의 spring.mvc.view.prefix, spring.mvc.view.suffix 설정정보를 사용해서 등록한다
 *
 * new-form이라는 뷰 이름으로 viewResolver를 호출
 * InternalResourceViewResolver -> InternalResourceView -> forward()
 */
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}