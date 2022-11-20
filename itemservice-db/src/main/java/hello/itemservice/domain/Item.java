package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity // jpa 객체로 인식
//@Table(name = "item")   // 객체명과 같으면 생략가능
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
