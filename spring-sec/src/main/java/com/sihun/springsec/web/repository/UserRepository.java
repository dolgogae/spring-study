package com.sihun.springsec.web.repository;

import com.sihun.springsec.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}
