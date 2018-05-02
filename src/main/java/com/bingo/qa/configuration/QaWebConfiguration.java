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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);

        //设置当访问user页面时,走此拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
    }
}
