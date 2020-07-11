package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.TempMeetingDAO;
import it.polimi.tiw.dao.UsersDAO;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public CheckLogin() {
        super();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsersDAO usersDAO = new UsersDAO(connection);
        User user = null;
        try {
            user = usersDAO.checkCredentials(email, password);
            new TempMeetingDAO(connection).cleanAllTempMeetingsByUserID(user.getId());
        } catch (SQLException e) {
            // throw new ServletException(e);
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database credential checking");
            return;
        }
        String path = getServletContext().getContextPath();
        if (user == null) {
            path = getServletContext().getContextPath() + "/index.html";
            request.getSession().setAttribute("loginError", "");
        } else {
            request.getSession().setAttribute("user", user);
            String target = "/HomePage";
            path = path + target;
        }
        response.sendRedirect(path);
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlException) {
        }
    }
}
