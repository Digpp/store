package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**收货持久层的接口*/
public interface AddressMapper {
    /**
     * 插入用户的收货地址数据
     * @param address 收货地址数据
     * @return 受影响行数
     */
    Integer insert(Address address);

    /**
     * 根据用户的id统计收货地址数量
     * @param uid 用户id
     * @return 当前用户的收货地址的总数
     */
    Integer countByUid(Integer uid);

    /**
     * 根据用户的uid查询用户的收货地址数据
     * @param uid 用户的uid
     * @return  对应的用户收货地址信息
     */
    List<Address> findByUid(Integer uid);

    /**
     * 根据aid查询收货地址数据
     * @param aid 收货地址id
     * @return 收货地址数据，如果没有则返回null值
     */
    Address findByAid(Integer aid);

    /**
     * 根据用户的uid值来修改用户的收货地址设置为非默认
     * @param uid 用户id
     * @return 受影响的行数
     */
    Integer updateNonDefaultByUid(Integer uid);

    /**
     * 将指定的收货地址设置为默认地址
     * @param aid 收货地址id
     * @param modifiedUser 修改执行人
     * @param modifiedTime 修改时间
     * @return 受影响的行数
     */
    Integer updateDefaultByAid(@Param("aid") Integer aid,
                               @Param("modifiedUser") String modifiedUser,
                               @Param("modifiedTime") Date modifiedTime);

    /**
     * 根据收货地址id删除数据
     * @param aid 收货地址id
     * @return 受影响的行数
     */
    Integer deleteByAid(Integer aid);

    /**
     * 查询某用户最后修改的收货地址
     * @param uid 归属的用户id
     * @return 该用户最后修改的收货地址，如果该用户没有收货地址数据则返回null
     */
    Address findLastModified(Integer uid);
}
