package it.polimi.tiw.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BlockAccess implements Filter {

    public BlockAccess() {
        super();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String homePagePath = req.getServletContext().getContextPath() + "/HomePage?error=Invalid request to server";

        res.sendRedirect(homePagePath);
    }


    public void init(FilterConfig fConfig) throws ServletException {
    }

}