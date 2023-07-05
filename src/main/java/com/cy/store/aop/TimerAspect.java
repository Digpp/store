package com.cy.store.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

@Aspect
@Component
public class TimerAspect {
    @Before("execution(* com.cy.store.service.impl.UserServiceImpl.login(..))")
    public void loginTime(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("用户登入时间:"+formatter.format(date));
    }

    @Around("execution(*  com.cy.store.service.impl.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录起始时间
        long start = System.currentTimeMillis();
        // 执行连接点方法，即切面所在位置对应的方法。本项目中表示执行注册或执行登录等
        Object result = pjp.proceed();
        // 记录结束时间
        long end = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("用户足迹行为时间:"+formatter.format(date));
        // 计算耗时
        System.err.println("用户行为耗时：" + (end - start) + "ms.");
        // 返回连接点方法的返回值
        return result;
    }
}