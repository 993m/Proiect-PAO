package databaseAccess;

import model.Rental;
import model.option.RentalOption;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalOptionCRUD implements  CRUD<RentalOption>{
    private static RentalOptionCRUD instance = null;
    private RentalOptionCRUD(){}
    public static RentalOptionCRUD getInstance(){
        if(instance==null){
            instance = new RentalOptionCRUD();
        }
        return instance;
    }

    @Override
    public void add(@NotNull RentalOption option) throws SQLException {
        String sqlQuery = "INSERT INTO `option` (description, price) VALUES (?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, option.getDescription());
        pstmt.setFloat(2, option.getPrice());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        option.setId(rs.getInt(1));
    }

    public void addForRental(@NotNull RentalOption option, @NotNull  Rental rental) throws SQLException {
        String sqlQuery = "INSERT INTO rental_option (rental_id, option_id) VALUES (?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, rental.getId());
        pstmt.setInt(2, option.getId());
        pstmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM option WHERE idOption=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void deleteForRental(@NotNull RentalOption option, @NotNull  Rental rental) throws SQLException {
        String sqlQuery = "DELETE FROM rental_option WHERE rental_id=? AND option_id=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, rental.getId());
        pstmt.setInt(2, option.getId());
        pstmt.executeUpdate();
    }

    @Override
    public RentalOption get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM option WHERE idOption=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public ArrayList<RentalOption> getAll() throws SQLException {
        ArrayList<RentalOption> options = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM option";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            options.add(mapResultSet(rs));
        return options;
    }

    @Override
    public RentalOption mapResultSet(@NotNull ResultSet rs) throws SQLException {
        RentalOption option = new RentalOption(rs.getString("description"), rs.getFloat("price"));
        option.setId(rs.getInt("idOption"));

        return option;
    }

    @Override
    public void update(int id, RentalOption updatedRentalOption) throws SQLException {
        // nu
    }

    public ArrayList<RentalOption> getAllForRental(@NotNull Rental rental) throws SQLException {
        ArrayList<RentalOption> options = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM `option` JOIN `rental_option` ON option.idOption=rental_option.option_id\n JOIN `addressoption` ON rental_option.option_id=addressoption.idAddressOption\n WHERE `rental_id`=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, rental.getId());
        ResultSet rs = pstmt.executeQuery();

        AddressRentalOptionCRUD addressRentalOptionCRUD = AddressRentalOptionCRUD.getInstance();
        while(rs.next())
            try{
                options.add(addressRentalOptionCRUD.mapResultSet(rs));
            }
            catch(SQLException e){
                options.add(mapResultSet(rs));
            }

        return options;
    }

    public ArrayList<RentalOption> getAllForProduct(@NotNull Product product) throws SQLException {
        ArrayList<RentalOption> options = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM `option` " +
                "JOIN `product_option` ON option.idOption=product_option.optionId " +
                "WHERE `productId`=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, product.getId());
        ResultSet rs = pstmt.executeQuery();

        AddressRentalOptionCRUD addressRentalOptionCRUD = AddressRentalOptionCRUD.getInstance();
        while(rs.next())
        {
            options.add(mapResultSet(rs));
        }
        return options;
    }

    public boolean checkIfAddressOption(@NotNull RentalOption option) throws SQLException {
        String sqlQuery = "SELECT * FROM `addressoption` " +
                "WHERE `idAddressOption`=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, option.getId());
        ResultSet rs = pstmt.executeQuery();


        int rowCount=0;
        while(rs.next()){
            rowCount++;
        }
        if(rowCount>0){
            return true;
        }
        return false;
    }
}
