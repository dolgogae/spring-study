package com.sihun.springsec.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 기존에는 WebSecurityConfigurerAdapter를 상속받아서 했지만,
 * 최신 spring 버전에서는 deprecated 되었다.
 * 따라서 아래처럼 변경하여 사용할 수 있다.
 *
 * 참고 사이트: https://covenant.tistory.com/277
 */
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)  // pre, post로 권한 체크를 하겠다.
public class SecurityConfig {

    /*
    기존 AuthenticationManagerBuilder
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password("2222")
                        .roles(("USER"))
                ).withUser(User.builder()
                        .username("user3")
                        .password("3333")
                        .roles(("ADMIN")));
    }*/
    /**
     * 변경 버전
     * 밑의 bean을 등록하게 되면, application.yml에 있는 자격들은 모두 없어지게 된다.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("2222"))
                .roles("USER")
                .build();
        UserDetails user3 = User.builder()
                .username("user3")
                .password(passwordEncoder().encode("3333"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user2, user3);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /* 변경 전

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests((requests)->
                    requests.antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                );
        http.formLogin();
        http.httpBasic();
    }
     */

    /**
     * 변경 버전
     *
     * 인증 여부와 관계없이 풀어주고 싶은 페이지를 등록하고 싶을때
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests((requests)->
                requests.antMatchers("/").permitAll()
                        .anyRequest().authenticated()
        );
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
