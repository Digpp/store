package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.service.ex.InsertEmptyException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressServiceTest {
    @Autowired
    private AddressService addressService;

    @Test
    public void addNewAddress(){
            Address address = new Address();
            address.setPhone("129381923123");
            address.setName("女朋友");
            addressService.addNewAddress(9, "管理员", address);
    }

    @Test
    public void setDefault(){
        addressService.setDefault(7,9,"Tom002");
    }

    @Test
    public void delete() {
        try {
            Integer aid = 9;
            Integer uid = 9;
            String username = "飒飒的";
            addressService.delete(aid, uid, username);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
