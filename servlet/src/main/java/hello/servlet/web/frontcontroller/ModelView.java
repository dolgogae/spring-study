package hello.servlet.web.frontcontroller;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 매핑하는 작업을 별도의 클래스로 분리해준다.(v3)
 *
 * model - key(String): 저장하는 이름 / value(Object): 저장하는 정보
 * 예를들어 member라는 객체를 담고 싶으면 member라는 String에 Member 객체에 해당하는 value가 있으면 된다.
 */
@Getter @Setter
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }
}
