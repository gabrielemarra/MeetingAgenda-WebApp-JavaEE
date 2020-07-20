package it.polimi.tiw.controllers;

import it.polimi.tiw.auxiliary.ConnectionManager;
import it.polimi.tiw.beans.MeetingWithInvitationsList;
import it.polimi.tiw.beans.TempMeeting;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.MeetingsDAO;
import it.polimi.tiw.dao.TempMeetingDAO;
import it.polimi.tiw.dao.UsersDAO;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/CheckInvitations")
@MultipartConfig
public class CheckInvitations extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public CheckInvitations() {
        super();
        // TODO Auto-generated constructor stub
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
        //collezione info riunione e user selezionati

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        TempMeetingDAO tMDao = new TempMeetingDAO(connection);

        //check synthax of form fields
        String formMeetingTitle = request.getParameter("meetingTitle");
        String formMeetingDateTime = request.getParameter("meetingDateTime");
        LocalDateTime dateTime = LocalDateTime.parse(formMeetingDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();

        try  {
            TempMeeting.checkPrimaryKeySyntax(formMeetingTitle, localDate, localTime);
            TempMeeting meetingCache = tMDao.getTempMeeting(formMeetingTitle, formMeetingDateTime, user.getId()); //local and cache parameters are both equal
            //no remaining attempts left
            if (meetingCache==null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (meetingCache.getAttempts() + 1 > 3) {
                cancelMeetingCreation(response, session, meetingCache);
                return;
            }
            //at least remaining attemps left

            //check guests
            //check that form fields are still the same
            Map<String, String[]> usersInRequest = request.getParameterMap(); // < ID, ID>
            //clean map from unwanted parameters
            usersInRequest = mapCleaner(usersInRequest);
            ArrayList<User> selectedUsersList = new ArrayList<>();
            Collection<String> selectedIDs = new ArrayList<>();

            for (String[] value : usersInRequest.values()) {
                selectedIDs.add(value[0]);
            }

            try {
                selectedUsersList = new UsersDAO(connection).getUsersFromId(selectedIDs);
                System.out.println(selectedUsersList);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            meetingCache.setAttempts(meetingCache.getAttempts()+1);
            new TempMeetingDAO(connection).setAttempts(meetingCache);

            if (meetingCache.getMaxParticipants() - 1 < selectedUsersList.size() && meetingCache.getAttempts() < 3) {
                cancelMeetingCreation(response, session, meetingCache);
            } else if (meetingCache.getMaxParticipants() - 1 >= selectedUsersList.size()) {
                completeMeetingCreation(response, session, meetingCache, selectedUsersList);
            } else {
                cancelMeetingCreation(response, session, meetingCache);
            }

        } catch(InvalidParameterException | SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void completeMeetingCreation(HttpServletResponse response, HttpSession session, TempMeeting tempMeeting, ArrayList<User> selectedUsersList) throws IOException, SQLException {
        MeetingWithInvitationsList meetingWithInvitationsList = new MeetingWithInvitationsList(tempMeeting, selectedUsersList);

        MeetingsDAO meetingsDAO = new MeetingsDAO(connection);
        try {
            meetingsDAO.addMeetingToDatabase(meetingWithInvitationsList);
            cleanTempDB(tempMeeting);
        } catch (SQLException throwables) {
            cancelMeetingCreation(response, session, tempMeeting);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("html/text");
        response.getWriter().println("Meeting created correctly");
    }

    private void cancelMeetingCreation(HttpServletResponse response, HttpSession session, TempMeeting tempMeeting) throws IOException, SQLException {
        cleanTempDB(tempMeeting);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void cleanTempDB(TempMeeting tempMeeting) throws SQLException {
        new TempMeetingDAO(connection).deleteTempMeeting(tempMeeting);
    }

    private Map<String, String[]> mapCleaner (Map<String, String[]> map) {
        Map<String, String[]> cleanedMap = new LinkedHashMap<>(map);
        cleanedMap.remove("meetingTitle");
        cleanedMap.remove("meetingDateTime");
        return cleanedMap;
    }



}
