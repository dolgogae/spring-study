package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * @Mapper로 MyBatis가 인식을 한다.
 * 인터페이스의 메서드를 호출하면 xml 파일을 실행해준다.
 * MyBatis spring 연동 모듈에서 @Mapper 조회해서 Mapper 인터페이스를 동적 프록시 객체를 만든다.
 * 동적 프록시 객체를 기반으로 spring bean에 등록한다.
 */
@Mapper
public interface ItemMapper {

    void save(Item item);

    void update(@Param("id") Long id, @Param("updateParam")ItemUpdateDto updateDto);

    List<Item> findAll(ItemSearchCond itemSearch);

    Optional<Item> findById(Long id);
}
