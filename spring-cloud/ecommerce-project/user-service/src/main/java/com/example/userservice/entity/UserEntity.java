package com.example.userservice.entity;

import com.example.userservice.entity.dto.UserDto;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity @Getter
//@Data   // 추후 수정
@Table(name = "users")
@ToString
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encyptedPwd;

    public UserEntity mappingUserDto(UserDto userDto){
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.userId = userDto.getUserId();
        return this;
    }

    // 암호화 로직이 없기 때문에 setter를 만들어줌
    public void setEncyptedPwd(String encyptedPwd) {
        this.encyptedPwd = encyptedPwd;
    }
}
