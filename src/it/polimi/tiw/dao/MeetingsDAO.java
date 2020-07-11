package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Meeting;
import it.polimi.tiw.beans.MeetingWithInvitationsList;
import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private int getLastID(Meeting meeting) throws SQLException {
        String query = "SELECT  id_meeting FROM meetings  WHERE title=? AND duration=? AND max_participants=? AND id_creator=? AND date_time = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, meeting.getTitle());
            preparedStatement.setInt(2, meeting.getDuration());
            preparedStatement.setInt(3, meeting.getMaxParticipants());
            preparedStatement.setInt(4, meeting.getIdCreator());
            preparedStatement.setString(5, meeting.getFormattedDateTime());
            try (ResultSet result = preparedStatement.executeQuery();) {

                if (result.next()) {
                    return result.getInt(1);
                } else {
                    System.out.println("Error");
                    throw new SQLException();
                    //todo remove
                }
            }
        }
    }

    public void addMeetingToDatabase(MeetingWithInvitationsList meetingWithInvitationsList) throws SQLException {
        Meeting meetingInfo = meetingWithInvitationsList.getRealMeeting();

        String query = "INSERT INTO meetings (`title`, `date_time`, `duration`, `max_participants`, `id_creator`) VALUES (?,?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, meetingInfo.getTitle());
            preparedStatement.setString(2, meetingInfo.getFormattedDateTime());
            preparedStatement.setInt(3, meetingInfo.getDuration());
            preparedStatement.setInt(4, meetingInfo.getMaxParticipants());
            preparedStatement.setInt(5, meetingInfo.getIdCreator());

            preparedStatement.executeUpdate();
            int meetingID = getLastID(meetingInfo);
            try {
                for (User user : meetingWithInvitationsList.getParticipantsList()) {
                    if(user.getId()!=meetingInfo.getIdCreator()) {
                        new InvitationDAO(connection).addInvitationToDatabase(meetingID, user);
                    }
                }
            } catch (SQLException e) {
                String deletionQuery = "DELETE FROM meetings WHERE (`id_meeting` = ?);";
                try (PreparedStatement deletionPreparedStatement = connection.prepareStatement(deletionQuery)) {
                    deletionPreparedStatement.setInt(1, meetingID);
                    deletionPreparedStatement.executeUpdate();
                }
                throw new SQLException();
            }
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
