package org.phillyete.quizzo.web.config;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class CORSFilter implements Filter {

    public CORSFilter() { }

    public void init(FilterConfig fConfig) throws ServletException { }

    public void destroy() {	}

    public void doFilter(
            ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        ((HttpServletResponse)response).addHeader(
                "Access-Control-Allow-Origin", "quizzo-ete.com:9000"
        );
        chain.doFilter(request, response);
    }
}