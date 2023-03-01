package hello.springmvc.basic.request;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;

/* request 변수를 받는 방법 */
@Slf4j
@Controller
public class RequsetParamController {

    /**
     * 가장 초기형태의 모습
     * 서블릿 객체를 통해서 직접 받아온다.
     */
    @RequestMapping("/requset-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody // return 값의 문자열을 결과값으로 화면에 뿌려준다. RestController가 어노테이션으로 되어있다면 따로 안해도 된다.
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            // 변수로 들어가는것이 요청으로 들어오는 변수의 이름이 된다.
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge){
        log.info("username={}, age={}", memberName, memberAge);

        return "ok";
    }

    /**
     * RequestParam의 변수가 요청 변수명과 동일하면 생략해도 된다.
     */
    @ResponseBody 
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age){
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * 추가적으로 요청변수명과 메서드에 들어오는 변수명이 동일하다면
     * RequestParam도 생략이 가능하다.
     * 하지만 가독성 부분에서 떨어질수도 있기때문에 잘 생각해서 써야한다.
     */
    @ResponseBody 
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username,int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * required가 false일 경우에는 꼭 멤버변수로 안들어와도 된다.(아래 예시 age 생략 가능)
     * 하지만 자바의 기본 변수형은 null 값을 넣을 수 없기 때문에 Integer같은 자료형을 써야한다.
     */
    @ResponseBody 
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username, 
            @RequestParam(required = false) Integer age){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * default value를 넣어주면 변수에 기본값이 들어간다.
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username, 
            @RequestParam(required = false, defaultValue = "-1") int age){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 모든 요청 파라미터를 받고 싶을때는 map을 통해서도 받을 수 있다.
     */
    @ResponseBody 
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            @RequestParam Map<String, Object> paramMap){

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    @ResponseBody 
    @RequestMapping("/request-attribute-v1-temp")
    public String modelAttributeV1Temp(@RequestParam String username, @RequestParam int age){
        HelloData helloData = new HelloData();
        helloData.setAge(age);
        helloData.setUsername(username);
        
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);
        return "ok";
    }

    /**
     * ModelAttribute를 통해서 변수를 받으면 helloData에 해당하는 property(getter, setter)를 찾는다.
     * 해당 property를 찾으면 이름을 통해서 매칭해주게 된다.
     * 따라서 따로 객체에 값을 넣어줄 필요가 없다.
     */
    @ResponseBody 
    @RequestMapping("/request-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData){
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);
        return "ok";
    }

    // ModelAttribute는 생략가능하다.
    // 단순한 타입: RequestParam, 다른 타입: ModelAttribute로 번역
    @ResponseBody 
    @RequestMapping("/request-attribute-v2")
    public String modelAttributeV2(HelloData helloData){
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);
        return "ok";
    }

}