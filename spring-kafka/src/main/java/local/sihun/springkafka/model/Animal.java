package local.sihun.springkafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;

@Getter
@ToString
public class Animal {

    private final String name;

    @Max(50)
    private final int age;

    public Animal(@JsonProperty("name") String name,
                  @JsonProperty("age") int age) {
        this.name = name;
        this.age = age;
    }
}
