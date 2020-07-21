package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Meeting;
import it.polimi.tiw.beans.MeetingWithInvitationsList;
import it.polimi.tiw.beans.User;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeetingsDAO {
    private Connection connection;

    public MeetingsDAO(Connection connection) {
        this.connection = connection;
    }

    public void addMeetingToDatabase(MeetingWithInvitationsList meetingWithInvitationsList) throws SQLException {
        Meeting meetingInfo = meetingWithInvitationsList.getRealMeeting();
        String query = "INSERT INTO meetings (`title`, `date_time`, `duration`, `max_participants`, `id_creator`) VALUES (?,?,?,?,?);";

        InvitationDAO invitationDAO = new InvitationDAO(connection);

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, meetingInfo.getTitle());
            preparedStatement.setString(2, meetingInfo.getFormattedDateTime());
            preparedStatement.setInt(3, meetingInfo.getDuration());
            preparedStatement.setInt(4, meetingInfo.getMaxParticipants());
            preparedStatement.setInt(5, meetingInfo.getIdCreator());

            preparedStatement.executeUpdate();
            int meetingID;

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                meetingID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating Meeting failed, no ID obtained.");
            }

            for (User user : meetingWithInvitationsList.getParticipantsList()) {
                if (user.getId() != meetingInfo.getIdCreator()) {
                    invitationDAO.addInvitationToDatabase(meetingID, user);
                }
            }
            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Meeting> getMeetingListCreatedByThisUser(int userID) throws SQLException {
        List<Meeting> myMeetings = new ArrayList<>();

        String nowTime = ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "SELECT  id_meeting, title, date_time, duration, max_participants FROM meetings  WHERE id_creator=? AND date_time > ?  ORDER BY date_time";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, nowTime);
            try (ResultSet result = preparedStatement.executeQuery();) {

                while (result.next()) {
                    Meeting tempMeeting = new Meeting(result.getInt("id_meeting"),
                            result.getString("title"),
                            result.getInt("duration"),
                            result.getInt("max_participants"),
                            userID,
                            result.getString("date_time"));
                    myMeetings.add(tempMeeting);
                }
                return myMeetings;
            }
        }
    }

    public List<Meeting> getMeetingListWithThisUserAsParticipant(int userID) throws SQLException {
        List<Meeting> meetings = new ArrayList<>();

        String nowTime = ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "SELECT  M.id_meeting, title, date_time, duration, max_participants, id_creator, U.displayed_name FROM meetings AS M, invitation as I, users as U " +
                "WHERE I.id_user=? AND M.id_meeting=I.id_meeting AND id_creator=U.id_user AND M.date_time > ? ORDER BY date_time";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, nowTime);
            try (ResultSet result = preparedStatement.executeQuery();) {
                while (result.next()) {
                    Meeting tempMeeting = new Meeting(result.getInt("id_meeting"),
                            result.getString("title"),
                            result.getInt("duration"),
                            result.getInt("max_participants"),
                            result.getInt("id_creator"),
                            result.getString("date_time"));
                    tempMeeting.setCreatorName(result.getString("displayed_name"));
                    meetings.add(tempMeeting);
                }
                return meetings;
            }
        }
    }
}
