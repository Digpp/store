package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DistrictServiceTest {
    @Autowired
    private DistrictService districtService;

    @Test
    public void getByParent() {
        // 86代表中国，所有省的代码都是86
        List<District> list= districtService.getByParent("86");
        for (District i:list){
            System.out.println(i);
        }
    }
}
