package com.cqut.addressBook.controller.filter;

import javax.servlet.*;
import java.io.IOException;
/**
 * @author Augenstern
 * @date 2021/12/6
 */
@javax.servlet.annotation.WebFilter(filterName = "BookFilter", urlPatterns = "/server/*")
public class BookFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        chain.doFilter(request, response);
    }
}
