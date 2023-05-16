package com.example.catalogservice.repository;

import com.example.catalogservice.entity.CatalogEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CatalogRepositoryTest {

    @Autowired CatalogRepository catalogRepository;

    @Test
    void findAllTest(){
        List<CatalogEntity> result = catalogRepository.findAll();

        for (CatalogEntity catalogEntity : result) {
            System.out.println(catalogEntity);
        }
    }
}