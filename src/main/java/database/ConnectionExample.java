package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alenka on 18.06.2016.
 */
public class ConnectionExample {

    String dbUrl = "jdbc:mysql://localhost:3306/sampledb";
    String username = "root";
    String password = "qwerty21";

    Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(dbUrl,username,password);

            if (connection != null) {
                System.out.println("System connected!");
            } else {
                System.out.println("No connection");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String username, String password, String fullname, String email) throws SQLException {

        String sql = "INSERT INTO users(username, password, fullname, email) VALUES (?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, fullname);
        statement.setString(4, email);
        int rowInserted = statement.executeUpdate();

        if (rowInserted > 0) {
            System.out.println("New user inserted");
        }
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();

        User user = new User();

        if (resultSet.next()) {

            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setFullname(resultSet.getString("fullname"));
        }

        return user;
    }

    public List<User> getUserList() throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> userArrayList = new ArrayList<User>();
        User tempUser;
        while (resultSet.next()) {
            tempUser = new User();
            tempUser.setUsername(resultSet.getString("username"));
            tempUser.setPassword(resultSet.getString("password"));
            tempUser.setEmail(resultSet.getString("email"));
            tempUser.setFullname(resultSet.getString("fullname"));
            userArrayList.add(tempUser);
        }

        return userArrayList;
    }



    public static void main(String[] args) {
        ConnectionExample connectionExample = new ConnectionExample();
        connectionExample.connect();
        try {
//            connectionExample.createUser("bill","pass","gates","bill.gates@gmail.com");
//            connectionExample.createUser("billy","passw","gates","billy.gates@gmail.com");

            User user = connectionExample.getUserById(1);
            System.out.println(user.getUsername() + " " + user.getFullname() );

            int userAmount = connectionExample.getUserList().size();
            System.out.println(userAmount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
