package com.sihun.springsec.web.controller;

import com.sihun.springsec.web.controller.dto.SecurityMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "홈페이지";
    }

    /**
     * 사용자가 어떤 권한과 어떤 인증으로 접근했는지 확인하는 방법
     */
    @RequestMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext()
                .getAuthentication();
    }

    /**
     * 개인정보에 해당하는 리소스를 확인
     *
     * PreAuthorize 어노테이션으로 접근 가능한 Role을 지정해 줄 수 있다.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage user(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("User 정보")
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public SecurityMessage admin(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("Admin 정보")
                .build();
    }
}
