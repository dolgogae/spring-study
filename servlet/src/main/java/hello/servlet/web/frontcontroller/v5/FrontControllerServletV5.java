package hello.servlet.web.frontcontroller.v5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

/**
 * 개발자마다 프로젝트마다 다른 controller방식을 이용하는게 선호될 수 있다.
 * 따라서 유연한 controller의 설계의 적용이 필요해 나온 것이 adapter이다.
 * 
 * 핸들러 매핑정보 -> 핸들러 어댑터 목록 -> 핸들러어댑터 -> 컨트롤러 
 * 다음과 같은 순서로 컨트롤러를 찾아오게 된다.
 */
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet{
    
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5(){
        initHandlerMappingMap();
        initHandlerAdapter();
    }

    private void initHandlerAdapter(){
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap(){
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Object handler = getHandler(request);
        if(handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        Map<String, String> paramMap = createParamMap(request);
       
        // 마지막으로 ModelView를 반환하면 기존과 같은 방식으로 렌더링을 시켜줄 수 있다.
        ModelView modelView = adapter.handle(request, response, paramMap);

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

    /**
     * 핸들러 매핑에서 핸들러를 찾아온다.
     * 생성자에서 미리 핸들러에서 등록되어 있다.
     * request url과 매핑해서 찾을 수 있다.
     */
    private Object getHandler(HttpServletRequest request){
        String requestUrl = request.getRequestURI();
        return handlerMappingMap.get(requestUrl);
    }

    /**
     * 핸들러 어댑터를 찾아온다.
     * 핸들러를 통해서 핸들러 어댑터를 찾을수 있다.
     */
    private MyHandlerAdapter getHandlerAdapter(Object handler){
        for(MyHandlerAdapter myHandlerAdapter: handlerAdapters){
            if(myHandlerAdapter.support(handler)){
                return myHandlerAdapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler: "+handler);
    }
}
