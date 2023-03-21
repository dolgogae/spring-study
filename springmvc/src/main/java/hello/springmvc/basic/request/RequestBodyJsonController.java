package hello.springmvc.basic.request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;

/* Json을 매칭 */

/**
 * HttpMessageConverter
 * http 요청, 응답 둘다 사용된다.
 * 요청과 응답에 있어서 http 형식으로 변경을 시켜준다.
 * 기존에 코딩했던 것들 중에 바로 객체를 반환하거나 요청변수를 바로 객체로 받아오는 부분에서 역할을 하게 된다.
 *
 * http 요청 데이터
 * 요청 메시지를 읽을 수 있는 지 확인하기 위해 canRead()를 호출
 * Content-Type 미디어 타입을 지원하는가?
 * *미디어타입: application/json...
 *
 * http 응답 데이터
 * 응답 메시지를 쓸 수 있는지 확인하기 위해 canWrite()를 호출
 * Accept 미디어 타입을 지원하는가?
 *
 * 핸들러 어댑터에서 해당 컨트롤러에 관련된 데이터를 만들어 던져주게 된다.
 * ArgumentResolver를 호출해서 필요한 파라미터의 값을 채워넣게 된다.
 * 반환값은 ReturnValueHandler에서 처리하게 된다.
 * 모두 인터페이스로 제공되기 때문에 WebMvcConfigurer를 사용해서 커스텀 확장이 가능하다.
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 가장 기본적인 방법
     */
    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}",messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    /**
     * 동일하게 RequestBody를 통새서 받을 수 있다.
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException{

        log.info("messageBody={}",messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * @RequestBody는 생략하면 안된다. -> 값이 들어가지 않는다.(@ModelAttribute가 기본적으로 들어가기 때문에 요청 파라미터를 가져가게 된다.)
     * 바로 HelloData라는 객체로 받는것도 가능하다.
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData){
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity){
        HelloData helloData = httpEntity.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * HelloData로 Json 형태로 떨궈주는것도 가능하다.
     * message converter가 반환하는 것도 적용이 가능하다.
     */
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData){
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        // helloData라는 객체가 생성된다.
        return helloData;
    }

}
