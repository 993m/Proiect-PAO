package databaseAccess;

import model.Address;
import model.Rental;
import model.option.AddressRentalOption;
import model.option.RentalOption;
import org.jetbrains.annotations.NotNull;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressRentalOptionCRUD implements CRUD<AddressRentalOption> {
    private static AddressRentalOptionCRUD instance = null;
    private AddressRentalOptionCRUD(){}
    public static AddressRentalOptionCRUD getInstance(){
        if(instance==null){
            instance = new AddressRentalOptionCRUD();
        }
        return instance;
    }

    @Override
    public void add(@NotNull AddressRentalOption option) throws SQLException {
        RentalOptionCRUD r = RentalOptionCRUD.getInstance();
        r.add(option);

        String sqlQuery = "INSERT INTO `addressoption` (idAddressOption, address_id) VALUES (?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, option.getId());
        pstmt.setInt(2, option.getAddress().getId());
        pstmt.executeUpdate();
    }

    public void addForRental(@NotNull AddressRentalOption option, @NotNull  Rental rental) throws SQLException {
        add(option);
        RentalOptionCRUD r = RentalOptionCRUD.getInstance();
        r.addForRental(option,rental);
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM addressoption WHERE idAddressOption=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();

        RentalOptionCRUD r = RentalOptionCRUD.getInstance();
        r.delete(id);
    }


    @Override
    public AddressRentalOption get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM addressoption WHERE idAddressOption=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public ArrayList<AddressRentalOption> getAll() throws SQLException {
        ArrayList<AddressRentalOption> options = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM addressoption";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            options.add(mapResultSet(rs));
        return options;
    }

    public ArrayList<AddressRentalOption> getAllForRental(@NotNull Rental rental) throws SQLException {
        ArrayList<AddressRentalOption> options = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM `addressoption`"+
                "JOIN `rental_option` ON rental_option.option_id=addressoption.idAddressOption" +
                " WHERE `rental_id`=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, rental.getId());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        while(rs.next())
            options.add(mapResultSet(rs));
        return options;
    }

    @Override
    public AddressRentalOption mapResultSet(@NotNull ResultSet rs) throws SQLException {
        AddressCRUD a = AddressCRUD.getInstance();
        RentalOptionCRUD r = RentalOptionCRUD.getInstance();
        Address adr = a.get(rs.getInt("address_id"));
        AddressRentalOption option = new AddressRentalOption(r.mapResultSet(rs), adr);
        return option;
    }

    @Override
    public void update(int id, AddressRentalOption updatedOption) throws SQLException {
        String sqlQuery = "UPDATE `addressoption` SET `address_id`=? WHERE `idAddressOption`=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, updatedOption.getAddress().getId());
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }



}
