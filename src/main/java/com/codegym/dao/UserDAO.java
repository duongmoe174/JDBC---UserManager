package com.codegym.dao;

import com.codegym.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{

    public static final String URLJDBC = "jdbc:mysql://localhost:3306/quanlyuser";
    public static final String USERDBNAME = "root";
    public static final String PASSWORDDB = "123456";

    private static final String INSERT_USERS_SQL ="INSERT INTO user (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from user where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from user where id = ?;";
    private static final String UPDATE_USERS_SQL = "update user set name = ?,email= ?, country =? where id = ?;";

    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URLJDBC, USERDBNAME, PASSWORDDB);
            System.out.println("Success!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fail connection");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
                )
        {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            printSQLException(e);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try(
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
                ) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");

                user = new User(id, name, email, country);
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<>();
      try (
              Connection connection = getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(
                      "SELECT  * FROM user "
              );
              ) {
          ResultSet resultSet = preparedStatement.executeQuery();
          while (resultSet.next()){
              int id = resultSet.getInt("id");
              String name  = resultSet.getString("name");
              String email = resultSet.getString("email");
              String country = resultSet.getString("country");

              User newUser = new User(id, name, email, country);

              users.add(newUser);
          }
      } catch (SQLException e) {
          printSQLException(e);
      }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDelete;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);
                ) {
            preparedStatement.setInt(1, id);
            rowDelete = preparedStatement.executeUpdate() > 0;
        }
        return rowDelete;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
                ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
           preparedStatement.setString(3, user.getCountry());
           preparedStatement.setInt(4, user.getId());

            rowUpdated =preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
