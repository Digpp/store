package com.cy.store.service;

import com.cy.store.entity.User;

/**用户模块业务层接口*/
public interface UserService {
    /**
     * 用户注册方法
     * @param user 用户的数据对象
     */
    void reg(User user);

    /**
     * 用户登录功能
     * @param username 用户名
     * @param password 用户的密码
     * @return 当前匹配的用户数据，如果没有则返回null值
     */
    User login(String username,String password);

    /**
     * 修改用户密码功能
     * @param uid 用户id
     * @param username 用户名
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     */
    void changePassword(Integer uid,String username,String oldPassword,String newPassword);

    /**
     * 根据用户的id查询数据
     * @param uid 用户id
     * @return 返回对应的用户数据
     */
    User getByUid(Integer uid);

    /**
     * 更新用户的信息数据操作
     * @param uid 用户的id
     * @param username 用户的名字
     * @param user 用户数据对象
     */
    void changeInfo(Integer uid , String username , User user);

    /**
     * 修改用户的头像
     * @param uid 用户id
     * @param avatar 用户头像路径
     * @param username 用户的名字
     */
    void changAvatar(Integer uid, String avatar,String username);
}
