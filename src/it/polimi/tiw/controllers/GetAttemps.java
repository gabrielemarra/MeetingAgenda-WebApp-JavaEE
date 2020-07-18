package it.polimi.tiw.controllers;

import it.polimi.tiw.auxiliary.ConnectionManager;
import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.TempMeetingDAO;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang.StringEscapeUtils.escapeJava;

@WebServlet("/GetAttempts")
@MultipartConfig
public class GetAttemps extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection connection;

    public GetAttemps() {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        TempMeetingDAO tMDao = new TempMeetingDAO(connection);

        //check synthax of form fields
        String formMeetingTitle = escapeJava(request.getParameter("meetingTitle"));
        String formMeetingDateTime = escapeJava(request.getParameter("meetingDateTime"));
        LocalDateTime dateTime = LocalDateTime.parse(formMeetingDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();

        TempMeeting.checkPrimaryKeySyntax(formMeetingTitle, localDate, localTime);
        TempMeeting meetingCache = null; //local and cache parameters are both equal
        try {
            meetingCache = tMDao.getTempMeeting(formMeetingTitle, formMeetingDateTime, user.getId());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("internal server error");
            return;
        }
        //no meeting cache
        if (meetingCache == null) {
            //sql error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("bad request, no meeting cache found");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(meetingCache.getAttempts());


    }

    private void cancelMeetingCreation(HttpServletResponse response, HttpSession session, TempMeeting tempMeeting) throws IOException, SQLException {
        new TempMeetingDAO(connection).deleteTempMeeting(tempMeeting);
    }


}
