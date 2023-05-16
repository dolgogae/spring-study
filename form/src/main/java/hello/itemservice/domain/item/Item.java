package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
// @Data DB와 연결되는 도메인에는 사용하지 않는 것이 좋다.
public class Item {

    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;

    private Boolean open; // 판매 여부
    private List<String> regions; // 등록 지역
    private ItemType itemType; //상품 종류
    private String deliveryCode; // 배송 종류

    public Item(){}

    public Item(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
