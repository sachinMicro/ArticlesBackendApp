package com.articles.security.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class RequestSourceFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

         System.out.println("FROM RequestSourceFilter");
        
        String sourceHeader = httpRequest.getHeader("X-Source-Verification");
        if (sourceHeader == null || !sourceHeader.equals("mobile-app")) {
            throw new RuntimeException("Unauthorized request source");
        }
        chain.doFilter(request, response);
    }
}