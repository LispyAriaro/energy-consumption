package com.quick.energy.consumption.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SimpleCORSFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String originHeader = request.getHeader("Origin");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,origin,accept,content-type,access-control-request-method,access-control-request-headers,authorization");

        if (!request.getMethod().equals("OPTIONS")) {
            try {
                chain.doFilter(req, res);
            } catch (ServletException e) {
                logger.error("ServletException during request processing (filter): ", e);
            } catch (IOException e) {
                logger.error("IOException during request processing (filter): ", e);
            }
        }
    }

    public void init(FilterConfig filterConfig) {
        // nothing to implement
    }

    public void destroy() {
        // nothing to implement
    }
}