package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 다음과 같이 view 이름의 중복을 제거해주는 로직을 추가했다(/WEB-INF/views/~~~.jsp)
 * 이를 위해서는 각 페이지에 들어가는 파라미터를 분리해줘야하기 때문에 다음처럼 paramMap을 사용하는 것을 볼 수 있다.
 *
 * 앞서 ModelView를 만들어서 각 viewName과 그에 맞는 파라미터를 찾아 넘겨줄 수 있다.
 */
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet{

    private final Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3(){
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        String requestUrl = request.getRequestURI();
        
        // /front-controller/v3/members
        ControllerV3 controller = controllerMap.get(requestUrl);

        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // paramMap
        Map<String, String> paramMap = createParamMap(request);
       
        ModelView modelView = controller.process(paramMap);

        String viewName = modelView.getViewName();
        MyView view = new MyView("/WEB-INF/views/"+viewName+"jsp");

        view.render(modelView.getModel(), request, response);
    }

    private Map<String, String> createParamMap(HttpServletRequest request){
        Map<String, String> paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
