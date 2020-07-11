package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Meeting;
import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InvitationDAO {
    private final Connection connection;

    public InvitationDAO(Connection connection) {
        this.connection = connection;
    }


    public void addInvitationToDatabase(int meetingID, User userToAdd) throws SQLException {
        String query = "INSERT INTO invitation (`id_meeting`, `id_user`) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, meetingID);
            preparedStatement.setInt(2, userToAdd.getId());

            preparedStatement.executeUpdate();
        }
    }
}
