package databaseAccess;

import model.product.Bicycle;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BicycleCRUD implements CRUD<Bicycle> {
    private static BicycleCRUD instance = null;
    private BicycleCRUD(){}
    public static BicycleCRUD getInstance(){
        if(instance == null){
            instance = new BicycleCRUD();
        }
        return instance;
    }

    @Override
    public Bicycle mapResultSet(@NotNull ResultSet rs) throws SQLException {
        ProductCRUD p = ProductCRUD.getInstance();
        Product product = p.mapResultSet(rs);
        return new Bicycle(product,
                Bicycle.Category.valueOf(rs.getString("category")),
                rs.getInt("wheelSize"),
                rs.getString("brakeSystem"));

    }

    @Override
    public Bicycle get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM bicycle JOIN product ON bicycle.idBicycle = product.idProduct WHERE idBicycle=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public ArrayList<Bicycle> getAll() throws SQLException {
        ArrayList<Bicycle> bikes = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM bicycle JOIN product ON bicycle.idBicycle = product.idProduct";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            bikes.add(mapResultSet(rs));
        return bikes;
    }

    @Override
    public void add(@NotNull Bicycle bike) throws SQLException {
        ProductCRUD p = ProductCRUD.getInstance();
        p.add(bike);

        String sqlQuery = "INSERT INTO bicycle (idBicycle, category, wheelSize, brakeSystem) VALUES (?,?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, bike.getId());
        pstmt.setString(2, bike.getCategory().toString());
        pstmt.setInt(3, bike.getWheelSize());
        pstmt.setString(4, bike.getBrakeSystem().toString());
        pstmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM bicycle WHERE idBicycle=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();

        ProductCRUD p = ProductCRUD.getInstance();
        p.delete(id);
    }

    @Override
    public void update(int id, Bicycle updatedBike) throws SQLException {
        // nu
    }
}
