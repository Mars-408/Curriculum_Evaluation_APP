package com.example;

import com.example.interceptors.PortalLoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootConfig implements WebMvcConfigurer {

    @Autowired
    PortalLoginCheckInterceptor portalLoginCheckInterceptor;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeUrl = new ArrayList<>();
        excludeUrl.add("/portal/user/login.do");
        //excludeUrl.add("/portal/user/register.do");
        //excludeUrl.add("/portal/user/resetPassword.do");
        //registry.addInterceptor(portalLoginCheckInterceptor).addPathPatterns("/portal/user/**").excludePathPatterns(excludeUrl);
        //registry.addInterceptor(portalLoginCheckInterceptor).addPathPatterns("/portal/stuevaluation/**");
        //registry.addInterceptor(portalLoginCheckInterceptor).addPathPatterns("/portal/teachercourse/**");
    }
}
