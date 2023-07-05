package com.cy.store.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**定义一个拦截器*/
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 检查全局session对象中是否有uid数据，如果有则放行，如果没有则重定向到登入页面
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器(url+Controller:映射)
     * @return 如果返回值为true表示放行当前请求，如果返回值为false则表示拦截当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //HttpServletRequest对象来获取全局的session对象
        if (request.getSession().getAttribute("uid")== null){
            //说明用户没有登入过系统，则重定向到login.html登入页面
            response.sendRedirect("/web/login.html");
            //结束后续的调用
            return false;
        }
        //请求放行
        return true;
    }
}
