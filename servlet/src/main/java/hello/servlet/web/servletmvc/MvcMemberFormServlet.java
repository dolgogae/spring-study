package hello.servlet.web.servletmvc;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 기존의 방법에는 한 개에서 비즈니스 로직과 뷰 렌더링을 모두 해결하려고 한다.
 * 둘에는 변경의 라이프 사이클이 다르다는 점에서 문제가 있다.
 *
 * spring mvc는 뷰와 렌더링 모델에 대한 것을 분리하고 비즈니스 로직 또한 따로 분하여 이러한 문제점을 해결한다.
 *
 * request, response가 사용되지 않는점과
 * 컨트롤러에서 공통 처리가 어렵다는 문제가 있다.(반복되는 것을 계속 호출해야하는 문제)
 */
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet{
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // WEB-INF: 외부에서 호출해도 호출되지 않는다. 항상 controller를 거쳐서 호출
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);

        // 다른 서블릿이나 jsp로 이동할 수 있는 기능.
        // 서버 내부에서 다시 호출이 발생한다.
        dispatcher.forward(request, response);
    }
}
