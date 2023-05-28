package databaseAccess;

import model.Address;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressCRUD implements CRUD<Address> {
    private static AddressCRUD instance = null;
    private AddressCRUD(){}
    public static AddressCRUD getInstance(){
        if(instance == null){
            instance = new AddressCRUD();
        }
        return instance;
    }

    @Override
    public void add(@NotNull Address a) throws SQLException {
        String sqlQuery = "INSERT INTO `address` (city, streetName, streetNumber) VALUES (?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, a.getCity());
        pstmt.setString(2, a.getStreetName());
        pstmt.setInt(3, a.getStreetNumber());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        a.setId(rs.getInt(1));
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM address WHERE idAddress=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public Address get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM address WHERE idAddress=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public void update(int id, @NotNull Address updatedAddress) throws SQLException {
        String sqlQuery = "UPDATE card SET city=?, streetName=?, streetNumber=? WHERE idAddress=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, updatedAddress.getCity());
        pstmt.setString(2, updatedAddress.getStreetName());
        pstmt.setInt(3, updatedAddress.getStreetNumber());
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
    }

    @Override
    public ArrayList<Address> getAll() throws SQLException {
        ArrayList<Address> addresses = new ArrayList<>(0);
        //String sqlQuery = "SELECT * FROM address WHERE idAddress NOT IN (SELECT address_id FROM addressoption)";
        String sqlQuery = "SELECT * FROM address";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            addresses.add(mapResultSet(rs));
        return addresses;
    }

    @Override
    public Address mapResultSet(@NotNull ResultSet rs) throws SQLException {
        Address a = new Address(rs.getString("city"), rs.getString("streetName"), rs.getInt("streetNumber"));
        a.setId(rs.getInt("idAddress"));
        return a;
    }



}
