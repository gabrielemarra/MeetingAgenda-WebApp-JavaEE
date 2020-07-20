package it.polimi.tiw.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.auxiliary.ConnectionManager;
import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.TempMeetingDAO;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/CheckMeetingParameters")
@MultipartConfig
public class CheckMeetingParameters extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection connection;

    public CheckMeetingParameters() {
        super();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User creatorUser = (User) session.getAttribute("user");

        if (creatorUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Invalid credentials");
            return;
        }
        String path = getServletContext().getContextPath();

        // fetch meeting parameters from the request object
        TempMeeting tempMeeting = new TempMeeting();

        try {
            tempMeeting.setTitle(StringEscapeUtils.escapeJava(request.getParameter("title")));
            tempMeeting.setDuration(Integer.parseInt(request.getParameter("duration")));
            tempMeeting.setMaxParticipants(Integer.parseInt(request.getParameter("limit")));
            tempMeeting.setIdCreator(creatorUser.getId());
            tempMeeting.setLocalDate(getDateFromRequestParameter(request.getParameter("date")));
            tempMeeting.setLocalTime(LocalTime.parse(request.getParameter("time")));
        } catch (DateTimeParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("invalid date/time");
            return;
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("invalid integer value");
            return;
        }

        try {
            //if meeting fetch has been successful
            TempMeeting.checkInsertedMeeting(tempMeeting);

            TempMeetingDAO tempMeetingDAO = new TempMeetingDAO(connection);
            try {
                tempMeetingDAO.addTempMeetingToDatabase(tempMeeting);

                Gson gson = new Gson();
                String json = gson.toJson(tempMeeting);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error");
            }
        } catch (InvalidParameterException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    private LocalDate getDateFromRequestParameter(String param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //convert String to LocalDate
        return LocalDate.parse(param, formatter);
    }
}
