package databaseAccess;

import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductCRUD implements CRUD<Product> {
    private static ProductCRUD instance = null;
    private ProductCRUD(){}
    public static ProductCRUD getInstance(){
        if(instance == null){
            instance = new ProductCRUD();
        }
        return instance;
    }

    @Override
    public Product mapResultSet(@NotNull ResultSet rs) throws SQLException {
        Product product = new Product(rs.getString("name"),
                rs.getFloat("price"),
                rs.getString("manufacturer"),
                rs.getString("model"));
        product.setId(rs.getInt("idProduct"));
        return product;
    }

    @Override
    public Product get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM product " +
                "WHERE idProduct=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }
/*
    @Override
    public ArrayList<Product> getAll() throws SQLException {
        ArrayList<Product> products = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM product";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            products.add(mapResultSet(rs));
        return products;
    }
 */
    @Override
    public ArrayList<Product> getAll() throws SQLException {
        ArrayList<Product> products = new ArrayList<>(0);

        // cars
        CarCRUD carCRUD = CarCRUD.getInstance();
        products.addAll(carCRUD.getAll());

        // bikes
        BicycleCRUD bicycleCRUD = BicycleCRUD.getInstance();
        products.addAll(bicycleCRUD.getAll());

        return products;
    }

    @Override
    public void add(@NotNull Product product) throws SQLException {
        String sqlQuery = "INSERT INTO product (name, manufacturer, model, price) VALUES (?,?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getManufacturer());
        pstmt.setString(3, product.getModel());
        pstmt.setFloat(4, product.getPrice());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        product.setId(rs.getInt(1));
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM product WHERE idProduct=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public void update(int id, Product updatedProduct) throws SQLException {
        // nu
    }


}
