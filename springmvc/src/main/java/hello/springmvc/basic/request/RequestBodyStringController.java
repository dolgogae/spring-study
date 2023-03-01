package hello.springmvc.basic.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
/* 일반 text */
@Slf4j
@Controller
public class RequestBodyStringController {

    /**
     * 가장 초기의 방법
     */
    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    /**
     * v1의 방법에서 InputStream을 그대로 받을 수 있다.
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException{
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        responseWriter.write("ok");
    }

    /**
     * message converter를 통해서 HttpEntity<자료형>를 바로 받을 수 있다.
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException{
        String messageBody = httpEntity.getBody();
        // HttpHeaders messageHeader = httpEntity.getHeaders();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<String>("ok");
    }

    @PostMapping("/request-body-string-v3-1")
    public HttpEntity<String> requestBodyStringV3_1(RequestEntity<String> httpEntity) throws IOException{
        String messageBody = httpEntity.getBody();
        // HttpHeaders messageHeader = httpEntity.getHeaders();
        log.info("messageBody={}", messageBody);

        return new ResponseEntity<String>("ok", HttpStatus.CREATED);
    }

    /**
     * 마지막으로 변형된 형태는 @RequestBody를 통해서 바로 받는게 가능하다.
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody){
        log.info("messageBody={}", messageBody);

        return "ok";
    }

}
