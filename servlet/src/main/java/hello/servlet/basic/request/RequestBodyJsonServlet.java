package hello.servlet.basic.request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StreamUtils;

import hello.servlet.basic.HelloData;

/**
 * API방식은 주로 JSON으로 많이 주고 받는다.
 * 맵핑되어야 할 객체가 필요하다.(HelloData)
 */
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet{

    /**
     * spring이 기본으로 제공해주는 jackson라이브러리에서 json을 매핑해준다.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // request에서 넘어온 데이터 읽는 부분
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        
        System.out.println("message body: "+messageBody);
        
        // 다음과 같이 객체에 있는 멤버 변수에 맵핑된다.
        // 이를 ObjectMapper를 통해서 매핑을 해줄 수 있다.
        // json 형식으로 보내줘야 파싱하는데 오류가 생기지 않는다.
        // 멤버변수와 json의 이름과 매핑이 된다.
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        System.out.println("HelloData.username: "+helloData.getUsername());
        System.out.println("HelloData.age: "+helloData.getAge());
        
        response.getWriter().write("ok");
    }
}
