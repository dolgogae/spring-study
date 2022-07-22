package hello.servlet.web.frontcontroller.v1;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListController;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveController;


@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet{
    
    private Map<String, ControllerV1> controllerMap = new HashMap<>();
    public FrontControllerServletV1(){
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveController());
        controllerMap.put("/front-controller/v1/members", new MemberListController());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        String requestUrl = request.getRequestURI();
        
        // /front-controller/v1/members
        ControllerV1 controller = controllerMap.get(requestUrl);

        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
