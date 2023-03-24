package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter @Getter
public class MemberDto {
    
    private String username;
    private int age;

    @QueryProjection
    public MemberDto(String username, int age){
        this.username = username;
        this.age = age;
    }
}
