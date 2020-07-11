package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.InvitationDAO;
import it.polimi.tiw.dao.TempMeetingDAO;
import it.polimi.tiw.dao.UsersDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
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
    private TemplateEngine templateEngine;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
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
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("notSelectedUsers", notSelectedUsers);
        ctx.setVariable("alreadySelectedUsers", alreadySelectedUsers);
        if (numberOfUsersToDeselect > 0) {
            ctx.setVariable("numberOfUsersToDeselect", numberOfUsersToDeselect);
        }
        ctx.setVariable("tempMeeting", tempMeeting);
        templateEngine.process(path, ctx, response.getWriter());
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
