package com.geostar.geostack.git_branch_manager.config;

import com.geostar.geostack.git_branch_manager.common.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器，并指定拦截的路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/login",
                        "/isLogin",
                        "/bootstrap-3.3.7/js/**",
                        "/css/**",
                        "/images/sort/**",
                        "/js/**",
                        "/static/**", // 不拦截存放静态资源的 /static 目录下的请求
                        "/resources/**" // 不拦截存放静态资源的 /resources 目录下的请求
                ); // 不拦截登录页面的请求
    }
}
