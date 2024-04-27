package com.geostar.geostack.git_branch_manager.common;

import com.geostar.geostack.git_branch_manager.config.GitRepositoryConfig;
import com.geostar.geostack.git_branch_manager.web.IndexController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 在请求处理之前进行登录状态检查
        // 这里可以根据具体的业务逻辑进行登录状态的判断，比如检查用户是否有登录凭证等
        boolean isLoggedIn = checkLoginStatus(request);

        // 如果用户未登录，则拦截请求，返回未登录提示或重定向到登录页面
        logger.info("isLoggedIn: {}", isLoggedIn);
        if (!isLoggedIn) {
            // 如果用户已登录，则放行请求，允许访问目标资源
            logger.info("sendRedirect to login");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置 401 Unauthorized 状态码
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 在请求处理之后但视图渲染之前执行的操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 在请求处理完成后执行的操作，包括清理资源等
    }

    // 检查用户登录状态的方法，具体实现根据业务逻辑而定
    private boolean checkLoginStatus(HttpServletRequest request) {
        // 这里可以根据具体的业务逻辑进行登录状态的判断，比如检查用户是否有登录凭证等
        // 返回 true 表示用户已登录，返回 false 表示用户未登录
        logger.info("gitUsername:{},gitPassword:{}", gitRepositoryConfig.getGitUsername(), gitRepositoryConfig.getGitPassword());
        return StringUtils.hasLength(gitRepositoryConfig.getGitUsername())
                && StringUtils.hasLength(gitRepositoryConfig.getGitPassword());
    }

    @Autowired
    private GitRepositoryConfig gitRepositoryConfig;
}
