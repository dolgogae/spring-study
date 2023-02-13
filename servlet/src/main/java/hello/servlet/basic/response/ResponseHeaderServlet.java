package hello.servlet.basic.response;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 서블릿 - HttpServletResponse - 기본 사용법
 * 헤더의 정보를 담아내는 것을 배우는 클래스
 */
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // status-line
        // http 응답 코드를 넣을 수 있다.
        response.setStatus(HttpServletResponse.SC_OK);

        // response-header
        // response.setHeader("Content-type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 캐시를 완전히 무효화
        response.setHeader("Pragma", "no-cache"); // 과거버전까지 캐시를 없앰
        response.setHeader("my-header", "hello"); // 사용자 정의 헤더

        content(response);
        cookie(response);
        redirect(response);
    
        response.getWriter().println("ok");
    }

    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8"); 
        response.setContentType("text/plain"); 
        response.setCharacterEncoding("utf-8"); 
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600; 
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600"); 
        Cookie cookie = new Cookie("myCookie", "good"); 
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }


    /**
     * location에 들어가는 것은 webapp에 있는 폴더로 매핑된다.
     */
    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
  }
}
