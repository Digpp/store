package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest:  表示当前类是测试类，打包时不会随项目一起被打包
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void reg(){
        try {
            User user = new User();
            user.setUsername("Tom002");
            user.setPassword("123");
            userService.reg(user);
            System.out.println(user.getUsername()+"创建成功");
        } catch (ServiceException e) {
            //获取类的对象，在获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login(){
        User user = userService.login("Tom002", "123");
        System.out.println(user);
    }

    @Test
    public void changPassword(){
        userService.changePassword(9,"管理员","321","123");
    }

    @Test
    public void getByUid(){
        System.out.println(userService.getByUid(9));
    }

    @Test
    public void changeInfo() {
        User user = new User();
        user.setPhone("15773101243");
        user.setEmail("lzw@qq.com");
        user.setGender(0);
        userService.changeInfo(9,"管理员",user);
    }

    @Test
    public void changAvatar(){
        userService.changAvatar(5,"/Avatar/*","管理员");
    }
}
