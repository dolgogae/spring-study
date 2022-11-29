package hello.servlet.basic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 서블릿은 기본적으로 HTTPServlet을 상속받아야 한다.
 * urlPattern 경로대로 생성된다.
 * 
 * 웹브라우져가 HTTP 요청, 응답 메시지를 생성한다.
 * 
 * HTTPServletRequest
 * 서블릿이 개발자 대신 요청을 받아 객체를 생성해주는 것
 * 
 * 
 */
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet{
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        // 여러 WAS 서버가 표준 서블릿(HttpServletRequest, HttpServletResponse)을 구현한다.

        System.out.println("HelloServlet.service");
        System.out.println("request: "+ request);
        System.out.println("response: "+ response);

        String username = request.getParameter("username");
        System.out.println("username = "+username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello" + username);
        
    }
}
