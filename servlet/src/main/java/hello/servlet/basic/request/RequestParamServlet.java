package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20
 * 
 * GET 방식을 이용한 파라미터 전송이다.
 * url에 전달하고 싶은 파라미터를 포함해서 전달한다.
 */
@WebServlet(name = "requestParamSevlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");

        // 모든 파라미터를 꺼낼 수 있다.(getParameterNames)
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName+ "=" + request.getParameter(paramName)));

        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username: "+ username);
        System.out.println("age: "+ age);
        System.out.println();

        // http://localhost:8080/request-param?username=hello&age=20&username=hello2
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username: "+name);
        }

        response.getWriter().write("ok");
    }
}
