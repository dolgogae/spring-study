package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * logging.level.hello.springmvc=trace
 * 프로퍼티에서 위의 변수를 통해 어느정도 레벨 이상의 로그를 출력할지 결정한다.
 * default는 info이다.
 *
 * 로그를 찍을때는 + 연산을 통해서 출력하면 안된다.
 * 왜냐면, 출력이 안되는 log까지 계산을 해서 추가적인 리소스를 사용하게 된다.
 */
@RestController
@Slf4j
public class LogTestController {

//    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);

        return "ok";
    }
}
