package com.example.catalogservice.service;

import com.example.catalogservice.entity.CatalogEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CatalogServiceImplTest {

    @Autowired CatalogService catalogService;

    @Test
    void getAllCatalogsTest(){
        Iterable<CatalogEntity> result = catalogService.getAllCatalogs();

        for (CatalogEntity catalogEntity : result) {
            System.out.println(catalogEntity);
        }
    }
}