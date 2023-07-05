package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressMapperTest {
    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void insert() {
        Address address = new Address();
        address.setUid(9);
        address.setPhone("19299371247");
        address.setName("管理员");
        addressMapper.insert(address);
    }

    @Test
    public void countByUid() {
        Integer count = addressMapper.countByUid(9);
        System.out.println(count);
    }

    @Test
    public void findByUid() {
        List<Address> list = addressMapper.findByUid(9);
        System.out.println(list);
    }

    @Test
    public void findByAid() {
        Address list = addressMapper.findByAid(24);
        System.out.println(list);
    }

    @Test
    public void updateNoeDefault() {
        System.out.println(addressMapper.updateNonDefaultByUid(9));

    }

    @Test
    public void updateDefaultByAid() {
        System.out.println(addressMapper.updateDefaultByAid(6, "Tom002", new Date()));
    }

    @Test
    public void deleteByAid() {
        Integer aid = 6;
        Integer rows = addressMapper.deleteByAid(aid);
        System.out.println("rows=" + rows);
    }

    @Test
    public void findLastModified() {
        Integer uid = 9;
        Address result = addressMapper.findLastModified(uid);
        System.out.println(result);
    }
}


