package it.polimi.tiw.debugutility;

import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testMain {

    public static void main(String[] args) {
        Connection connection = null;

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/meeting-agenda?serverTimezone=UTC";
        String userDB = "meeting-agenda-user";
        String passwordDB = "meetingagenda";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        User user1 = new User();
        user1.setId(9);


//        try {
//            System.out.println("\n");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

    }
}
