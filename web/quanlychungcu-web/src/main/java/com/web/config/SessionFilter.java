package com.web.config;

import com.web.entity.Resident;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*") // Áp dụng cho tất cả các URL
public class SessionFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String currentPath = httpRequest.getRequestURI();
        // Các URL không cần kiểm tra session
        if (currentPath.equals("/") || currentPath.equals("/login")
                || currentPath.startsWith("/css/") || currentPath.startsWith("/js/")) {
            chain.doFilter(request, response); // Tiếp tục xử lý
            return;
        }
        // Kiểm tra session
        if (httpRequest.getSession().getAttribute("resident") == null) {
            // Chưa đăng nhập, chuyển hướng về trang login
            httpResponse.sendRedirect("/login");
        } else {
            // Đã đăng nhập, tiếp tục xử lý
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
