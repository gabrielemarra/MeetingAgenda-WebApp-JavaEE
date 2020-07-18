package it.polimi.tiw.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.auxiliary.ConnectionManager;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UsersDAO;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
@WebServlet("/GetUserData")
public class GetUserData extends HttpServlet {

    public GetUserData() {
        super();
    }

    public void init() throws ServletException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson((User) session.getAttribute("user")));
    }
}
