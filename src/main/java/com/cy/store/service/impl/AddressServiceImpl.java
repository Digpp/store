package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.AddressService;
import com.cy.store.service.DistrictService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**新增收货地址的实现类*/
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    // 在添加用户的收货地址的业务层依赖于DistrictService的业务层接口
    @Autowired
    private DistrictService districtService;
    @Value("${user.address.max-count}")
    private Integer maxCount;   //配置文件中设置的最大收货地址限制

    @Transactional
    @Override
    public void addNewAddress(Integer uid , String username , Address address){
        Integer count = addressMapper.countByUid(uid);
        if (count >= maxCount){
            throw new AddressCountLimitException("用户收货地址超出限制");
        }
        // 对address对象中的数据进行补全: 省市区
        String ProvinceName = districtService.getNameByCode(address.getProvinceCode());
        String CityName = districtService.getNameByCode(address.getCityCode());
        String AreaName = districtService.getNameByCode(address.getAreaCode());
        address.setProvinceName(ProvinceName);
        address.setCityName(CityName);
        address.setAreaName(AreaName);

        // Uid,isDefault
        address.setUid(uid);
        Integer isDefault = count == 0 ? 1 :0;   //1表示默认,0表示非默认
        address.setIsDefault(isDefault);

        // 补全4项日志，创建时间及修改时间
        address.setCreatedUser(username);
        address.setModifiedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedTime(new Date());

        // 插入收货地址的方法
        Integer rows = addressMapper.insert(address);
        if (rows != 1){
            throw new InsertException("插入用户收货地址产生未知异常");
        }
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> list = addressMapper.findByUid(uid);
        return list;
    }

    @Override
    public void setDefault(Integer aid, Integer uid, String username) {
        // 根据参数aid，调用addressMapper中的findByAid()查询收货地址数据
        Address result = addressMapper.findByAid(aid);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出AddressNotFoundException
            throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
        }

        // 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
        if (!result.getUid().equals(uid)) {
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("非法访问的异常");
        }

        // 调用addressMapper的updateNonDefaultByUid()将该用户的所有收货地址全部设置为非默认，并获取返回受影响的行数
        Integer rows = addressMapper.updateNonDefaultByUid(uid);
        // 判断受影响的行数是否小于1(不大于0)
        if (rows < 1) {
            // 是：抛出UpdateException
            throw new UpdateException("设置默认收货地址时出现未知错误[1]");
        }

        // 调用addressMapper的updateDefaultByAid()将指定aid的收货地址设置为默认，并获取返回的受影响的行数
        rows = addressMapper.updateDefaultByAid(aid, username, new Date());
        // 判断受影响的行数是否不为1
        if (rows != 1) {
            // 是：抛出UpdateException
            throw new UpdateException("设置默认收货地址时出现未知错误[2]");
        }
    }

    @Transactional
    @Override
    public void delete(Integer aid, Integer uid, String username) {
        // 根据参数aid，调用findByAid()查询收货地址数据
        Address result = addressMapper.findByAid(aid);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出AddressNotFoundException
            throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
        }

        // 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
        if (!result.getUid().equals(uid)) {
            // 是：抛出AccessDeniedException：非法访问
            throw new AccessDeniedException("非法访问");
        }

        // 根据参数aid，调用deleteByAid()执行删除
        Integer rows1 = addressMapper.deleteByAid(aid);
        if (rows1 != 1) {
            throw new DeleteException("删除收货地址数据时出现未知错误，请联系系统管理员");
        }

        // 判断查询结果中的isDefault是否为0
        if (result.getIsDefault() == 0) {
            return;
        }

        // 调用持久层的countByUid()统计目前还有多少收货地址
        Integer count = addressMapper.countByUid(uid);
        // 判断目前的收货地址的数量是否为0
        if (count == 0) {
            return;
        }

        // 调用findLastModified()找出用户最近修改的收货地址数据
        Address lastModified = addressMapper.findLastModified(uid);
        // 从以上查询结果中找出aid属性值
        Integer lastModifiedAid = lastModified.getAid();
        // 调用持久层的updateDefaultByAid()方法执行设置默认收货地址，并获取返回的受影响的行数
        Integer rows2 = addressMapper.updateDefaultByAid(lastModifiedAid, username, new Date());
        // 判断受影响的行数是否不为1
        if (rows2 != 1) {
            // 是：抛出UpdateException
            throw new UpdateException("更新收货地址数据时出现未知错误，请联系系统管理员");
        }
    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address address = addressMapper.findByAid(aid);
        if (address == null) {
            throw new AddressNotFoundException("收货地址数据不存在异常");
        }
        if (!address.getUid().equals(uid)){
            throw new AccessDeniedException("非法访问数据异常");
        }
        address.setProvinceCode(null);
        address.setCityCode(null);
        address.setAreaCode(null);
        address.setCreatedUser(null);
        address.setCreatedTime(null);
        address.setModifiedUser(null);
        address.setModifiedTime(null);
        return address;
    }
}
