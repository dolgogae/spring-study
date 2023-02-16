package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * v1과 다른점은 마지막에 controller.process를 호출할 때 view를 반환받고
 * 화면 렌더링의 역할을 MyView로 넘겨주었다는 것이다.
 *
 * v1에서는 각각의 controller들이 view의 역할까지 맡아 주고 있었다.
 *
 * frontcontroller -> controller -> view 반환
 * view -> jsp -> html응답
 */
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet{

    private final Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2(){
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        String requestUrl = request.getRequestURI();
        
        // /front-controller/v2/members
        ControllerV2 controller = controllerMap.get(requestUrl);

        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyView view = controller.process(request, response);
        // render를 호출하게 되면 view 클래스 안의 dispatcher에 의해서 렌더링 된다.
        view.render(request, response);
    }
}
