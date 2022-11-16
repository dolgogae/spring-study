package hello.itemservice.repository;

import lombok.Data;

/**
 * 최종적으로 repository에 포함되어있는 DTO이기 때문에 
 * 패키지를 repository밑에 두는 것이 좀 더 좋다.
 */
@Data
public class ItemUpdateDto {
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
