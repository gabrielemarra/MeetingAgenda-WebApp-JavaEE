package it.polimi.tiw.controllers;

import it.polimi.tiw.auxiliary.ConnectionManager;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UsersDAO;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/SignUp")
@MultipartConfig
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void init() throws ServletException {
        try {
            connection = ConnectionManager.tryConnection(getServletContext());
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pwMain, pwConfirm;
        pwMain = request.getParameter("password1");
        pwConfirm = request.getParameter("password2");
        if(!pwMain.equalsIgnoreCase(pwConfirm)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("passwords don't match");
            return;
        } else {

            String email = request.getParameter("email");
            if (validateEmail(email)) {
                UsersDAO usersDAO = new UsersDAO(connection);
                User newBorn = new User();
                newBorn.setEmail(email);
                newBorn.setDisplayedName("TestUser" + new Random().nextInt(256));
                newBorn.setPassword(pwMain);
                try {
                    usersDAO.addNewUser(newBorn);
                    request.getSession().setAttribute("user", newBorn);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().println(email);
                } catch (SQLException e) {
                    //sql error
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("internal server error");
                    return;
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("please insert a valid email address");
                return;
            }
        }


        /*
                <label for="email-input">Email:</label>
        <input type="email" name="email" id="email-input-signup" placeholder="email@meeting.com"><br>
        <label for="password-input">Password:</label>
        <input type="password" name="password1" id="password-input-signup" placeholder="password"><br>
        <label for="password-input">Confirm Password:</label>
        <input type="password" name="password2" id="password-confirm-input-signup" placeholder="password"><br>
        <input type="submit" value="Sign Up">
         */

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

}
