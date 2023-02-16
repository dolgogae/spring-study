package hello.servlet.web.frontcontroller.v5;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hello.servlet.web.frontcontroller.ModelView;

public interface MyHandlerAdapter {
 
    // 컨트롤러가 핸들러를 지원하는 지 여부
    boolean support(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
