package hello.servlet.web.frontcontroller.v5.adapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter{

    @Override
    public boolean support(Object handler) {
        // TODO Auto-generated method stub
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {
        // 앞서 support에서 걸렀기 때문에 업캐스팅을 일부 허용해주는 부분이다.
        ControllerV3 controller = (ControllerV3)handler;

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request){
        Map<String, String> paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
    
}
