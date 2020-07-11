package it.polimi.tiw.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticatedChecker implements Filter {

    public AuthenticatedChecker() {
        super();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String loginPath = req.getServletContext().getContextPath() + "/index.html";

        HttpSession session = req.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            System.out.println("Invalid session, redirect to index.html");
            res.sendRedirect(loginPath);
            return;
        }
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }


    public void init(FilterConfig fConfig) throws ServletException {
    }

}