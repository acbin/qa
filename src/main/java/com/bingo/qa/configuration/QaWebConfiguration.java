package com.bingo.qa.configuration;


import com.bingo.qa.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

@Component
public class QaWebConfiguration implements WebMvcConfigurer{
    @Autowired
    PassportInterceptor passportInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
    }
}
