package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import com.sun.corba.se.impl.protocol.AddressingDispositionException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

public class BaseController {
    //响应成功，返回状态码200
    public static final int OK = 200;

    //请求处理方法，这个方法的返回值需要传递给前端
    //自动将异常对象传递给此方法的参数列表上
    //当项目出现异常，被统一拦截到此方法中，这个方法此时充当请求处理方法，方法的放回值直接给前端
    @ExceptionHandler({ServiceException.class,FileUploadException.class}) //用于统一处理抛出异常
    public JsonResult<Void> handException(Throwable e){
        JsonResult<Void> result = new JsonResult<>();
        if (e instanceof UsernameDuplicateException){
            result.setState(4000);
            result.setMassage("用户名以被注册");
        } else if (e instanceof UserNotFoundException) {
            result.setState(4001);
            result.setMassage("用户数据不存在异常");
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(4002);
            result.setMassage("用户密码不存在异常");
        } else if (e instanceof AddressCountLimitException) {
            result.setState(4003);
            result.setMassage("用户收货地址超出上限异常");
        }else if (e instanceof AddressNotFoundException) {
            result.setState(4004);
            result.setMassage("用户收货地址数据不存在异常");
        }else if (e instanceof AccessDeniedException) {
            result.setState(4005);
            result.setMassage("收货地址数据非法访问异常");
        } else if (e instanceof ProductNotFoundException) {
            result.setState(4006);
            result.setMassage("商品未找到异常");
        } else if (e instanceof CartNotFoundException) {
            result.setState(4007);
            result.setMassage("购物车数据不存在异常");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMassage("插入数据产生未知异常");
        } else if (e instanceof UpdateException) {
            result.setState(5001);
            result.setMassage("更新数据产生未知异常");
        } else if (e instanceof DeleteException) {
            result.setState(5002);
            result.setMassage("删除用户收货地址产生未知异常");
        } else if (e instanceof FileEmptyException) {
            result.setState(6000);
            result.setMassage("上传的文件为空的异常");
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
            result.setMassage("上传的文件的大小超出限制值异常");
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
            result.setMassage("文件类型异常");
        } else if (e instanceof FileStateException) {
            result.setState(6003);
            result.setMassage("上传的文件状态异常");
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
            result.setMassage("上传文件时读写异常");
        }
        return result;
    }

    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登入的用户uid的值
     */
    protected final Integer getuidFromSession(HttpSession session){
        //Integer.valueOf()可以将基本类型int转换为包装类型Integer，或者将String转换成Integer，String如果为Null或“”都会报错
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * 获取session对象中的username
     * @param session session对象
     * @return 当前登入的用户名
     */
    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
