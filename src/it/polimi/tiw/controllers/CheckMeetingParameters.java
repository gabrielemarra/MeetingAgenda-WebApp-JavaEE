package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.TempMeetingDAO;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/CheckMeetingParameters")
public class CheckMeetingParameters extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection connection;

    public CheckMeetingParameters() {
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
        response.sendRedirect(getServletContext().getContextPath() + "/HomePage?error=Invalid request to the server");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User creatorUser = (User) session.getAttribute("user");
        String path = getServletContext().getContextPath();

        // fetch meeting parameters from the request object
        TempMeeting tempMeeting = new TempMeeting();

        try {
            tempMeeting.setTitle(request.getParameter("title"));
            tempMeeting.setDuration(Integer.parseInt(request.getParameter("duration")));
            tempMeeting.setMaxParticipants(Integer.parseInt(request.getParameter("limit")));
            tempMeeting.setIdCreator(creatorUser.getId());
            tempMeeting.setLocalDate(getDateFromRequestParameter(request.getParameter("date")));
            tempMeeting.setLocalTime(LocalTime.parse(request.getParameter("time")));
        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            path += "/HomePage?error=";
            path += "Invalid Arguments";
            response.sendRedirect(path);
            return;
        }

        //if meeting fetch has been successful
        try {
            TempMeeting.checkInsertedMeeting(tempMeeting);
            request.setAttribute("tempMeetingInfo", tempMeeting);
            RequestDispatcher rd = request.getRequestDispatcher("/Invite");
            TempMeetingDAO tmdao = new TempMeetingDAO(connection);
            try {
                tmdao.addTempMeetingToDatabase(tempMeeting);
                rd.forward(request, response);
            } catch (SQLException e) {
                path += "/HomePage?error=";
                path += "Error writing temporary meeting to database";
                response.sendRedirect(path);
            }
        } catch (InvalidParameterException e) {
            //redirect the client to Homepage
            path += "/HomePage?error=";
            path += e.getMessage();
            response.sendRedirect(path);
        }
    }

    private LocalDate getDateFromRequestParameter(String param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //convert String to LocalDate
        return LocalDate.parse(param, formatter);
    }
}
