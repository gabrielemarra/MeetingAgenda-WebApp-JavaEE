package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UsersDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/SignUp")
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

    private void sendTextResponse (String res, HttpServletResponse response) {
        response.setContentType("text/plain");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(res);
        out.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pwMain, pwConfirm;
        pwMain = request.getParameter("password1");
        pwConfirm = request.getParameter("password2");
        if(!pwMain.equalsIgnoreCase(pwConfirm)) {
            sendTextResponse("password mismatch", response);
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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