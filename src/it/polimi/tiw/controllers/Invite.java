package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UsersDAO;

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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@WebServlet("/Invite")
public class Invite extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int numberOfUsersToDeselect;
        try {
            numberOfUsersToDeselect = (int) request.getAttribute("numberOfUsersToDeselect");
        } catch (NullPointerException e) {
            numberOfUsersToDeselect = 0;
        }

        List<User> notSelectedUsers = null;
        try {
            notSelectedUsers = getNotSelectedUsers(request);
        } catch (SQLException throwables) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in user database extraction");
        }
        List<User> alreadySelectedUsers = (ArrayList<User>) request.getAttribute("selectedUsers");

        TempMeeting tempMeeting = (TempMeeting) request.getAttribute("tempMeetingInfo");

        String path = "/WEB-INF/Invite.html";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(getServletContext().getContextPath() + "/HomePage?error=Invalid request to the server");
    }

    private List<User> getNotSelectedUsers(HttpServletRequest request) throws SQLException {
        UsersDAO usersDAO = new UsersDAO(connection);
        User user = (User) request.getSession().getAttribute("user");

        if (request.getAttribute("selectedUsers") == null) {
            return usersDAO.getUsersToInvite(user.getId());
        } else {
            List<User> allAvailableUsers = usersDAO.getUsersToInvite(user.getId()); //all the available users
            List<User> alreadySelectedUsers = (ArrayList<User>) request.getAttribute("selectedUsers"); //the ones I've selected

            Map<Integer, User> allAvailableHash = new LinkedHashMap<>(); //O(1) :P :P :P :P
            allAvailableHash = allAvailableUsers.stream().collect(Collectors.toMap(User::getId, Function.identity()));

            for (User u : alreadySelectedUsers) {
                allAvailableHash.remove(u.getId());
            }
            Collection<User> notSelectedUsersSuper = allAvailableHash.values();
            List<User> notSelectedUsers = new ArrayList<>(notSelectedUsersSuper);
            return notSelectedUsers;
        }
    }
}
