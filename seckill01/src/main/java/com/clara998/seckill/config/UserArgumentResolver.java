package com.clara998.seckill.config;

import com.alibaba.druid.util.StringUtils;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author clara
 * @date 2020/8/7
 * HandlerMethodArgumentResolver负责处理Handler方法里面的所有入参
 * 它是HandlerMethod方法的解析器，将HttpServletRequest(header + body 中的内容)解析为HandlerMethod方法的参数
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    @Override
    //是否支持类型
    public boolean supportsParameter(MethodParameter methodParameter) {
        //Class<?> 代表通配泛型
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        //request中找token->user
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        //在redis中token->User并且更新有效期
        return userService.getByToken(response, token);
    }

    //Cookie中有很多字段例如Userid , token, nickname等等
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
