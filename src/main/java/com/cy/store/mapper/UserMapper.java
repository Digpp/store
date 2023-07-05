package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

//持久层接口
public interface UserMapper {
    /**
     * 插入用户的数据
     * @param user
     * @return
     */
    Integer insert(User user);

    /**
     * 根据用户名查询数据
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     *  根据用户的uid来修改用户密码
     * @param uid 用户的id
     * @param password 用户输入的新密码
     * @param modifiedUser 表示修改的执行者
     * @param modifiedTime 表示修改的时间
     * @return 返回值为受影响的行数
     */
    Integer updatePasswordByUid(Integer uid, String password, String modifiedUser, Date modifiedTime);

    /**
     * 根据用户的uid查询用户数据
     * @param uid 用户的id
     * @return 返回对应用户的对象，没找到则返回null值
     */
    User findByUid(Integer uid);

    /**
     * 更新用户的数据信息
     * @param user 用户的信息
     * @return 返回值为受影响的行数
     */
    Integer updateInfoByUid(User user);

    /**
     * 根据用户的uid的值来修改用户的头像
     * @param uid
     * @param avatar
     * @param modifiedUser
     * @param modifiedTime
     * @return
     */
    Integer updateAvatarByUid(Integer uid, String avatar, String modifiedUser, Date modifiedTime);
}
