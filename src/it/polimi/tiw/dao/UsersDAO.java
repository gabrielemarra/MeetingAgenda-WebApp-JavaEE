package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersDAO {
    private Connection connection;

    public UsersDAO(Connection connection) {
        this.connection = connection;
    }

    public User checkCredentials(String email, String password) throws SQLException {
        String query = "SELECT  id_user, email, displayed_name FROM users  WHERE email = ? AND password =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet result = preparedStatement.executeQuery();) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    User user = new User();
                    user.setId(result.getInt("id_user"));
                    user.setEmail(result.getString("email"));
                    user.setDisplayedName(result.getString("displayed_name"));
                    return user;
                }
            }
        }
    }

    public List<User> getUsersToInvite(int userID) throws SQLException {
        List<User> usersToInvite = new ArrayList<>();

        String query = "SELECT id_user, displayed_name  FROM users  WHERE id_user!=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, userID);
            try (ResultSet result = preparedStatement.executeQuery();) {
                while (result.next()) {
                    User tempUser = new User(result.getInt("id_user"),
                            result.getString("displayed_name"));

                    usersToInvite.add(tempUser);
                }
                return usersToInvite;
            }
        }
    }

    public User getUserByID(int id) throws SQLException {
        String query = "SELECT displayed_name, id_user  FROM users  WHERE id_user=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, id);
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                User resUser = new User(result.getInt("id_user"),
                        result.getString("displayed_name"));
                return resUser;
            }
        }
    }

    public ArrayList<User> getUsersFromId(Collection<String> IDs) throws SQLException, NumberFormatException {
        ArrayList<User> result = new ArrayList<>();
        for (String id : IDs) {
            User cur = getUserByID(Integer.parseInt(id));
            result.add(cur);
        }

        return result;
    }
}
