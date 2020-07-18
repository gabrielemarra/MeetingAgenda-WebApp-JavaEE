package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.Meeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.MeetingsDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/HomePage")
public class Homepage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public Homepage() {
    }

    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        MeetingsDAO meetingsDAO = new MeetingsDAO(connection);

        List<Meeting> meetingsCreated = null;
        List<Meeting> meetingsAsParticipant = null;
        try {
            meetingsCreated = meetingsDAO.getMeetingListCreatedByThisUser(user.getId());
            meetingsAsParticipant = meetingsDAO.getMeetingListWithThisUserAsParticipant(user.getId());
        } catch (SQLException e) {
            // throw new ServletException(e);
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in Meetings lists extraction");
        }

        String path = "/WEB-INF/HomePage.html";
        ServletContext servletContext = getServletContext();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
