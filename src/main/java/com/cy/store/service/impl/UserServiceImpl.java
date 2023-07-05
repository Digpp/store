package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.UserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

/**用户模块业务层的实现类*/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     *  注册用户功能
     * @param user 用户的数据对象
     */
    @Override
    public void reg(User user) {
        String username = user.getUsername();
        //查询用户名是否存在
        User byUsername = userMapper.findByUsername(username);
        if (byUsername != null){
            //判断用户名是否以存在，存在则无法插入，抛出异常
            throw new UsernameDuplicateException("用户名以被注册");
        }

        //密码加密处理: md5算法形式,盐值 + password +盐值 ----盐值是一个随机的字符串
        //拿到用户密码
        String oldPassword = user.getPassword();
        //获取盐值(随机生成一个盐值)  UUID的java工具类生成随机盐值再转为字符串，再全部转为小写
        String salt = UUID.randomUUID().toString().toUpperCase();
        //  补全数据:盐值的记录
        user.setSalt(salt);
        //将密码和盐值进行加密处理
        String ma5Password = getMD5Password(oldPassword,salt);
        //将加密后的用户密码重新补齐到user对象中
        user.setPassword(ma5Password);

        //  补齐数据:is_delete设置为0
        user.setIsDelete(0);
        //  补全数据:4个日志字段信息
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        //  当前时间信息
        Date date =new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        //执行注册业务功能的实现
        Integer rows = userMapper.insert(user);
        if (rows != 1){
            throw new InsertException("用户注册时发生了未知异常");
        }
    }

    /**
     * 登入用户功能
     * @param username 用户名
     * @param password 用户的密码
     * @return
     */
    @Override
    public User login(String username, String password) {
        // 根据用户名称来查询用户的数据是否存在，如果不在则抛出异常
        User result = userMapper.findByUsername(username);
        if (result == null){
            throw new UserNotFoundException("用户数据不存在");
        }
        //1.检查用户的密码是否匹配
        //获取到数据库中加密后的密码
        String oldPassword = result.getPassword();
        //2.和用户传递过来的密码进行比较
        //先获取盐值: 上一次自动生成的盐值
        String salt = result.getSalt();
        //将用户的密码按照相同的md5算法的规则进行加密
        String newMd5Password = getMD5Password(password,salt);
        //将加密后的密码与数据库中的被加密的密码进行比较
        if (!newMd5Password.equals(oldPassword)){
            throw new PasswordNotMatchException("用户密码错误");
        }

        //判断is_delete字段值是否为1,标记为1则以被输出
        if (result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }

        //对查询到的需要数据进行封装，反之则不封装，提升系统性能
        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    /**
     * 修改密码功能
     * @param uid 用户id
     * @param username 用户名
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     */
    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        // 将原始密码和数据库密码进行对比
        String oldMD5Password = getMD5Password(oldPassword, result.getSalt());
        if (!oldMD5Password.equals(result.getPassword())){
            throw new PasswordNotMatchException("密码错误");
        }
        // 将新密码设置到数据库中，再将数据库中的新密码进行加密再更新
        String newMD5Password = getMD5Password(newPassword, result.getSalt());
        Integer rows= userMapper.updatePasswordByUid(uid, newMD5Password, username, new Date());
        if (rows != 1){
            throw new UpdateException("更新数据产生未知异常");
        }
    }

    /**
     * 根据用户id获取用户信息
     * @param uid 用户id
     * @return 用户信息
     */
    @Override
    public User getByUid(Integer uid) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        //对查询到的需要数据进行封装，反之则不封装，提升系统性能
        User user = new User();
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender(result.getGender());
        return user;
    }

    /**
     * 更新用户信息功能
     * @param uid 用户的id
     * @param username 用户的名字
     * @param user 用户数据对象
     */
    @Override
    public void changeInfo(Integer uid, String username, User user) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        user.setUid(uid);
        user.setModifiedUser(username); //修改者
        user.setModifiedTime(new Date());

        Integer rows = userMapper.updateInfoByUid(user);
        if (rows !=1){
            throw new UpdateException("更新数时产生未知异常");
        }
    }

    @Override
    public void changAvatar(Integer uid, String avatar, String username) {
        User result = userMapper.findByUid(uid);
        if (result == null|| result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        Integer rows = userMapper.updateAvatarByUid(uid, avatar, username, new Date());
        if (rows != 1){
            throw new UpdateException("更新用户数据时产生未知异常");

        }
    }

    /**
     *  定义一个md5算法的加密处理
     * @param password 用户密码
     * @param salt 随机盐值
     * @return 返回加密后的密码
     */
    private String getMD5Password(String password,String salt){
        for (int i=0;i<3;i++){
            //使用md5加密算法方法的调用(进行三次加密)
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        return password;
    }
}