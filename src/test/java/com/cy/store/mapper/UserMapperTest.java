package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

//@SpringBootTest:  表示当前类是测试类，打包时不会随项目一起被打包
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void inserts(){
        User user = new User();
        user.setUsername("Jay");
        user.setPassword("123");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void findByUsername(){
        User user =userMapper.findByUsername("Ja");
        System.out.println(user);
    }

    @Test
    public void updatePasswordByUid(){
        userMapper.updatePasswordByUid(5,"321","管理员",new Date());
    }

    @Test
    public void findByUid(){
        System.out.println(userMapper.findByUid(4));
    }

    @Test
    public void updateInfoByUid(){
        User user = new User();
        user.setUid(9);
        user.setPhone("1559126804");
        user.setEmail("1559126804@qq.com");
        user.setGender(1);
        userMapper.updateInfoByUid(user);
    }
    @Test
    public void updateAvatarByUid(){
        userMapper.updateAvatarByUid(9,"/update/avater.png","管理员",new Date());
    }
}
