package com.bingo.qa.configuration;


import com.bingo.qa.Interceptor.LoginRequiredInterceptor;
import com.bingo.qa.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;


@Component
public class QaWebConfiguration implements WebMvcConfigurer{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    /**
     * 注册拦截器(判断用户是谁，用户有没有权限)
     * 先注册passportInterceptor，再注册loginRequiredInterceptor
     * @param registry 注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 访问所有页面都需要对用户的ticket进行验证
        registry.addInterceptor(passportInterceptor);

        //设置当访问user页面时,走此拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
    }
}
