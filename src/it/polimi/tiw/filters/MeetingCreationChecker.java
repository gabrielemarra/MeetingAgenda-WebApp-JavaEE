package it.polimi.tiw.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MeetingCreationChecker implements Filter {

    public MeetingCreationChecker() {
        super();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String homePagePath = req.getServletContext().getContextPath() + "/HomePage";

        if (request.getParameter("meetingTitle") == null || request.getParameter("meetingDateTime") == null) {
            homePagePath += "?error=Invalid request to server";
            res.sendRedirect(homePagePath);
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }


    public void init(FilterConfig fConfig) throws ServletException {
    }

}