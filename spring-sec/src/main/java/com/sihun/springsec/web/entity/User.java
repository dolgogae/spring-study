package com.sihun.springsec.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class User {

    @Id
    private long id;
    private String username;
    private String paasword;
    private String authority;
}
