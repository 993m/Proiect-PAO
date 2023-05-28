package databaseAccess;

import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class UserCRUD implements CRUD<User> {
    private static UserCRUD instance = null;
    private UserCRUD(){}
    public static UserCRUD getInstance(){
        if(instance == null)
            instance = new UserCRUD();
        return instance;
    }

    @Override
    public User mapResultSet(@NotNull ResultSet rs) throws SQLException {
        User user = new User(rs.getString("username"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"));
        user.setId(rs.getInt("idUser"));
        return user;
    }

    @Override
    public User get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM user WHERE idUser=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    public User getUserByUsername(String username) throws SQLException {
        String sqlQuery = "SELECT * FROM user WHERE username=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, username);

        try{
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return mapResultSet(rs);
        }
        catch(SQLException e){
            System.out.println("This username does not exist!");
        }

        return null;
    }

    @Override
    public void add(@NotNull User user) throws SQLException {
        String sqlQuery = "INSERT INTO user (username, firstName, lastName, email) VALUES (?,?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getFirstName());
        pstmt.setString(3, user.getLastName());
        pstmt.setString(4, user.getEmail());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        user.setId(rs.getInt(1));
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM user WHERE idUser=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public void update(int id, @NotNull User updatedUser) throws SQLException {
        String sqlQuery = "UPDATE user SET username=?, firstName=?, lastName=?, email=? WHERE idUser=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, updatedUser.getUsername());
        pstmt.setString(2, updatedUser.getFirstName());
        pstmt.setString(3, updatedUser.getLastName());
        pstmt.setString(4, updatedUser.getEmail());
        pstmt.setInt(5, id);
        pstmt.executeUpdate();
    }

    @Override
    public ArrayList<User> getAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM user";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            users.add(mapResultSet(rs));
        return users;
    }

}
