package devcolibri;


import java.awt.image.AreaAveragingScaleFilter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alenka on 22.06.2016.
 */
public class mySqlConnection {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/devcolibri";
    static final String DB_NAME = DB_CONNECTION.split("/")[3];
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "qwerty21";
    static final String TABLE_DEFAULT = "DBUSER";

    private static Connection dbConnection;

    public static class User { // это сделано специально,а не вынесенно в отдельный класс

        String userId;
        String username;
        String createdBy;
        String createdDate;

        public User() {
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }
    }

    public static void main(String[] argv) {
        try {

//            createDbUserTable(TABLE_DEFAULT);
            insertIntoDbUserTable(TABLE_DEFAULT, "01", "BillGates", "System");
            deleteUserByUserId(01);
            updateUserByUserId(12,"SteaveJobs");
            if (1==0) throw new SQLException(); // заглушка для блока трай-катч

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date today = new Date();
        return dateFormat.format(today.getTime());
    }

    public static void getDBConnection() {
        try {
            System.out.println("Trying connect to DB " + DB_NAME);
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
            if (dbConnection != null)
                System.out.println("Connection to " + DB_NAME + " succesful!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<User> getUserList() throws SQLException {
        ArrayList<User> userList = new ArrayList<User>();
        User tempUser = null;
        String getUserSql = "SELECT * FROM " + TABLE_DEFAULT;
        System.out.println("Trying execute sql request with querry: "+ getUserSql);
        PreparedStatement preparedStatement = dbConnection.prepareStatement(getUserSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tempUser = new User();
            tempUser.setUserId(resultSet.getString("USER_ID"));
            tempUser.setUsername(resultSet.getString("USERNAME"));
            tempUser.setCreatedBy(resultSet.getString("CREATED_BY"));
            tempUser.setCreatedDate(resultSet.getString("CREATED_DATE"));
            userList.add(tempUser);
        }
        System.out.println("Loading userList from " + DB_NAME + "." + TABLE_DEFAULT + " succesful. Load "+ userList.size()+" users profile.");
        for (User user: userList) {
            System.out.println(user.getUserId()+ " : " + user.getUsername());
        }
        return userList;
    }

    private static void createDbUserTable(String tableName) throws SQLException {
        getDBConnection();
        Statement statement = null;
        String createTableSQL = "CREATE TABLE " + tableName+ "("
                + "USER_ID INT(5) NOT NULL, "
                + "USERNAME VARCHAR(20) NOT NULL, "
                + "CREATED_BY VARCHAR(20) NOT NULL, "
                + "CREATED_DATE DATE NOT NULL, " + "PRIMARY KEY (USER_ID) "
                + ")";
        try {
            System.out.println("Trying execute sql request with querry: "+ createTableSQL);
            statement = dbConnection.createStatement();
            statement.execute(createTableSQL); //             выполнить SQL запрос
            System.out.println("Table " + tableName + " is created!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public static void insertIntoDbUserTable(String tableName, String userId, String username, String createdBy) throws SQLException {
        boolean existUser = false;
        getDBConnection();
        Statement statement = null;
        ArrayList<User> userList = getUserList();

        for (User user: userList) {
            if (user.getUserId().equals(userId)) {
                existUser = true;
                userId = String.valueOf(Integer.parseInt(userId) + 1);
                System.out.println("Пользователь с таким userId существует, создаем такого же но с +1 = " + userId);
            }
        }

        String insertUserSQL = "INSERT INTO "+ tableName
                + "(USER_ID, USERNAME, CREATED_BY, CREATED_DATE) " + "VALUES"
                + "("+userId+",'"+username+"','"+createdBy+"','"+ getCurrentTimeStamp()+"')";
        System.out.println("Trying execute sql request with querry: "+ insertUserSQL);

        try {
            statement = dbConnection.createStatement();
            statement.execute(insertUserSQL);
            System.out.println(username+" added to table "+tableName + " with id=" + userId + ", created by "+ createdBy+ ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }

    }

    public static void deleteUserByUserId(int userId) throws SQLException {

        getDBConnection();
        Statement statement = null;
        String deleteUserSQL = "DELETE FROM DBUSER WHERE USER_ID = " + String.valueOf(userId);
        System.out.println("Trying execute sql request with querry: "+ deleteUserSQL);


        try {
            statement = dbConnection.createStatement();
            statement.execute(deleteUserSQL);
            System.out.println("User with User_ID = "+ userId + " deleted from table " + DB_NAME + "." + TABLE_DEFAULT +";");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }

    }

    public static void updateUserByUserId(int userId, String newName) throws SQLException {

        getDBConnection();
        Statement statement = null;
        String updateUserSQL = "UPDATE DBUSER SET USERNAME = '" + newName + "' WHERE USER_ID = " + String.valueOf(userId);
        System.out.println("Trying execute sql request with querry: "+ updateUserSQL);


        try {
            statement = dbConnection.createStatement();
            statement.execute(updateUserSQL);
            System.out.println("User with User_ID = "+ userId + " changed userName to " + newName + " in "+ DB_NAME + "." + TABLE_DEFAULT +";");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }

    }

}
