package it.polimi.tiw.dao;

import it.polimi.tiw.beans.TempMeeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TempMeetingDAO {
    private Connection connection;

    public TempMeetingDAO(Connection connection) {
        this.connection = connection;
    }

    public synchronized void addTempMeetingToDatabase(TempMeeting tempMeeting) throws SQLException {

        String query = "INSERT INTO temp_meetings (`title`, `date_time`, `duration`, `max_participants`, `id_creator`, `attempts`) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tempMeeting.getTitle());
            preparedStatement.setString(2, tempMeeting.getFormattedDateTime());
            preparedStatement.setInt(3, tempMeeting.getDuration());
            preparedStatement.setInt(4, tempMeeting.getMaxParticipants());
            preparedStatement.setInt(5, tempMeeting.getIdCreator());
            preparedStatement.setInt(6, tempMeeting.getAttempts());

            preparedStatement.executeUpdate();
        }
    }

    public synchronized TempMeeting getTempMeeting(String title, String formattedDateTime, int idCreator) throws SQLException {
        TempMeeting tempMeeting = null;

        String query = "SELECT * FROM temp_meetings WHERE title = ? AND date_time=? AND id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, formattedDateTime);
            preparedStatement.setInt(3, idCreator);
            try (ResultSet result = preparedStatement.executeQuery()) {

                if (result.next()) {
                    tempMeeting = new TempMeeting(result.getString("title"),
                            result.getString("date_time"),
                            result.getInt("id_creator"),
                            result.getInt("max_participants"),
                            result.getInt("duration"),
                            result.getInt("attempts"));
                }
                return tempMeeting;
            }
        }
    }

    public synchronized int getAttempt(String title, String formattedDateTime, int idCreator) throws SQLException {
        int attempt = -1;

        String query = "SELECT attempts FROM temp_meetings WHERE title = ? AND date_time=? AND id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, formattedDateTime);
            preparedStatement.setInt(3, idCreator);
            try (ResultSet result = preparedStatement.executeQuery()) {

                if (result.next()) {
                    attempt = result.getInt("attempts");
                }
                return attempt;
            }
        }
    }

    public synchronized void setAttempts(TempMeeting tempMeeting) throws SQLException {

        String query = "UPDATE temp_meetings SET attempts = ? WHERE title = ? AND date_time=? AND id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, tempMeeting.getAttempts());
            preparedStatement.setString(2, tempMeeting.getTitle());
            preparedStatement.setString(3, tempMeeting.getFormattedDateTime());
            preparedStatement.setInt(4, tempMeeting.getIdCreator());

            preparedStatement.executeUpdate();
        }
    }

    public synchronized void increaseAttempts(TempMeeting tempMeeting) throws SQLException {
        String query = "UPDATE temp_meetings SET attempts = ? WHERE title = ? AND date_time=? AND id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, tempMeeting.getAttempts()+1);
            preparedStatement.setString(2, tempMeeting.getTitle());
            preparedStatement.setString(3, tempMeeting.getFormattedDateTime());
            preparedStatement.setInt(4, tempMeeting.getIdCreator());

            preparedStatement.executeUpdate();
        }
    }

    public synchronized void deleteTempMeeting(TempMeeting tempMeeting) throws SQLException {
        String query = "DELETE FROM temp_meetings WHERE title = ? AND date_time=? AND id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tempMeeting.getTitle());
            preparedStatement.setString(2, tempMeeting.getFormattedDateTime());
            preparedStatement.setInt(3, tempMeeting.getIdCreator());

            preparedStatement.executeUpdate();
        }
    }

    public synchronized void cleanAllTempMeetingsByUserID(int idCreator) throws SQLException {
        String query = "SELECT * FROM temp_meetings WHERE id_creator=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idCreator);
            try (ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {
                    TempMeeting tempMeeting = new TempMeeting(result.getString("title"),
                            result.getString("date_time"),
                            result.getInt("id_creator"),
                            result.getInt("max_participants"),
                            result.getInt("duration"),
                            result.getInt("attempts"));
                    deleteTempMeeting(tempMeeting);
                }
            }
        }
    }
}
