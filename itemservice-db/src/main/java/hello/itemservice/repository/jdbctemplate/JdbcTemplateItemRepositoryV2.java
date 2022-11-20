package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTempalte
 *
 * 순서대로 파라미터가 바인딩 되는데
 * 만약 프로그래머의 실수로 섞인다면 오류가 많이 발생한다.
 * 그리고 데이터베이스의 데이터를 복구해야하기 때문에 버그를 해결하기 쉽지 않다.
 *
 * 모호함을 제거해서 코드를 명확히 만들어 유지보수가 쉽게 만들어야 한다.
 *
 * 이것을 해결해주는 것이 이름 지정 바인딩이다.
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

//    private final JdbcTemplate template;
    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }


    /**
     * BeanPropertySqlParameterSource
     * 이름 기반으로 한번에 파라미터를 생성해준다.
     * 자바빈 규약을 통해서 생성된다.
     * => 동일한 이름으로 파라미터를 지정해주어야 한다.
     *
     * 항상 사용할 수 있는 것은 아니다.
     * 예를 들어 아래 update에서는 id가 매핑되지 않는다. 따라서 사용할 수 없다.
     */
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (:itemName,:price,:quantity)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(item);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }

    /**
     * MapSqlParameterSource
     * Map과 유사하지만 SQL에 좀 더 특화되어 있다.
     * 메서드 체인을 통해서 편리함을 제공한다
     */
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);

        template.update(sql,param);
    }

    /**
     * 단순히 map을 만들어서 넘겨줘도 된다.
     */
    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id=:id";
        try{
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name as itemName, price, quantity from item";
        // Dynamic Query
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());
    }

    /**
     * 이름이 동일한 것을 바탕으로 자바빈 규약을 통해서 알아서 RowMapper를 만들어준다.
     * 하지만 item_name의 경우에는 getItem_name()으로 생성될 수도 있다.
     * 이런 경우에는 별칭을 이용해서 item_name as itemName을 사용하면 된다.
     *
     * 하지만 오랜 관례의 불일치로 인해서 카멜케이스-스네이크케이스는 알아서 변환해준다.
     */
    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
