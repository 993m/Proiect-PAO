package databaseAccess;

import model.Rental;
import model.User;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class RentalCRUD implements CRUD<Rental> {
    private static RentalCRUD instance = null;
    private RentalCRUD(){}
    public static RentalCRUD getInstance(){
        if(instance == null){
            instance = new RentalCRUD();
        }
        return instance;
    }

    @Override
    public Rental mapResultSet(@NotNull ResultSet rs) throws SQLException {
        ProductCRUD p = ProductCRUD.getInstance();
        UserCRUD u = UserCRUD.getInstance();
        AddressCRUD a = AddressCRUD.getInstance();

        Rental rental = new Rental(p.get(rs.getInt("product_id")),
                u.get(rs.getInt("user_id")),
                rs.getDate("startDate"),
                rs.getDate("endDate"),
                a.get(rs.getInt("pickUpAddress_id")),
                a.get(rs.getInt("returnAddress_id")));

        rental.setId(rs.getInt("idRental"));
        return rental;
    }

    @Override
    public void add(@NotNull Rental rental) throws SQLException {
        String sqlQuery = "INSERT INTO rental (product_id, user_id, startDate, endDate, totalPrice, pickUpAddress_id, returnAddress_id) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, rental.getProduct().getId());
        pstmt.setInt(2, rental.getUser().getId());
        pstmt.setDate(3, new java.sql.Date(rental.getStartDate().getTime()));
        pstmt.setDate(4, new java.sql.Date( rental.getEndDate().getTime()));
        pstmt.setFloat(5, rental.getTotalPrice());
        pstmt.setInt(6, rental.getPickUpAddress().getId());
        pstmt.setInt(7, rental.getReturnAddress().getId());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        rental.setId(rs.getInt(1));
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM rental WHERE idRental=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public Rental get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM user WHERE idRental=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public void update(int id, Rental newRental) throws SQLException {
        String sqlQuery = "UPDATE rental SET product_id=?, startDate=?, endDate=?, pickUpAddress_id=?, returnAddress_id=? WHERE idRental=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, newRental.getProduct().getId());
        pstmt.setDate(2,new java.sql.Date(newRental.getStartDate().getTime()));
        pstmt.setDate(3, new java.sql.Date(newRental.getEndDate().getTime()));
        pstmt.setInt(4, newRental.getPickUpAddress().getId());
        pstmt.setInt(5, newRental.getReturnAddress().getId());
        pstmt.setInt(6, id);
        pstmt.executeUpdate();
    }

    public ArrayList<Rental> getAllForUser(@NotNull User user) throws SQLException {
        ArrayList<Rental> rentals = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM rental WHERE user_id=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, user.getId());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
            rentals.add(mapResultSet(rs));
        return rentals;
    }

    @Override
    public ArrayList<Rental> getAll() throws SQLException {
        ArrayList<Rental> rentals = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM rental";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            rentals.add(mapResultSet(rs));
        return rentals;
    }

    public ArrayList<Rental> getRentalsForProduct(@NotNull Product product) throws SQLException{
        ArrayList<Rental> rentals = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM rental WHERE product_id=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, product.getId());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
            rentals.add(mapResultSet(rs));

        return rentals;
    }


}
