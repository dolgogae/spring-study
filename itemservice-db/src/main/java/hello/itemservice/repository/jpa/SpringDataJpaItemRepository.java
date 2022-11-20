package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * spring Data JPA는 동적쿼리에 약하다.
 * 이 문제는 QueryDSL을 이용해서 해결할 수 있다.
 */
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemName(String itemName);
    List<Item> findByPriceLessThanEqual(Integer price);

    //쿼리 메서드
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //쿼리 직접 실행
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);

    List<Item> findByItemNameLike(String itemName);
}
