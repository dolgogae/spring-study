package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * v3에 비해서 controller가 viewName만 반환한다.
 * controller를 작성하는 입장에서 훨씬 편해진다.
 * 
 * 다음과 같이 modelView를 따로 사용하지 않는다.
 * model을 파라미터로 넘겼다.
 */
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet{

    private final Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4(){
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        String requestUrl = request.getRequestURI();
        
        // /front-controller/v4/members
        ControllerV4 controller = controllerMap.get(requestUrl);

        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        Map<String, Object> model = new HashMap<>();

        request.getParameterNames().asIterator()
        .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));


        String viewName = controller.process(paramMap, model);
        MyView view = new MyView("/WEB-INF/views/"+viewName+"jsp");

        view.render(model, request, response);
    }
}
