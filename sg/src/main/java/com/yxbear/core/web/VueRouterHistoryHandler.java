package com.yxbear.core.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VueRouterHistoryHandler implements HandlerInterceptor {

    final Set<String> vueRouters;

    public VueRouterHistoryHandler(List<String> vueRouters) {
        super();
        this.vueRouters = new HashSet<>();
        if (vueRouters != null) {
            this.vueRouters.addAll(vueRouters);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        if (vueRouters.contains(servletPath)) {
            request.getRequestDispatcher("index.html").forward(request, response);
            response.setStatus(HttpStatus.OK.value());
            return false;
        } else if (servletPath.endsWith("/")) {
            // 拦截以斜杠结尾的请求，可以在这里进行相应的处理
            // 例如重定向到去除斜杠的路径
            response.sendRedirect(request.getRequestURI() + "index.html");
            return false;
        }
        return true;
    }

}
