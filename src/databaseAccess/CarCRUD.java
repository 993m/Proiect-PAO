package databaseAccess;

import model.product.Car;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarCRUD implements CRUD<Car> {
    private static CarCRUD instance = null;
    private CarCRUD(){}
    public static CarCRUD getInstance(){
        if(instance == null){
            instance = new CarCRUD();
        }
        return instance;
    }

    @Override
    public Car mapResultSet(@NotNull ResultSet rs) throws SQLException {
        ProductCRUD p = ProductCRUD.getInstance();
        Product product = p.mapResultSet(rs);
        return new Car(product, rs.getInt("seats"),
                Car.Transmission.valueOf(rs.getString("transmission")),
                rs.getInt("horsepower"),
                Car.EngineType.valueOf(rs.getString("engineType")),
                Car.Drivetrain.valueOf(rs.getString("drivetrain")),
                Car.Class.valueOf(rs.getString("carClass")),
                rs.getInt("kmLimit"),
                rs.getInt("speedLimit"));

    }

    @Override
    public Car get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM car JOIN product ON car.idCar = product.idProduct WHERE idCar=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public ArrayList<Car> getAll() throws SQLException {
        ArrayList<Car> cars = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM car JOIN product ON car.idCar = product.idProduct";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            cars.add(mapResultSet(rs));
        return cars;
    }

    @Override
    public void add(@NotNull Car car) throws SQLException {
        ProductCRUD p = ProductCRUD.getInstance();
        p.add(car);

        String sqlQuery = "INSERT INTO car (idCar, seats, transmission, horsepower, engineType, drivetrain, carClass, knLimit, speedLimit) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, car.getId());
        pstmt.setInt(2, car.getSeats());
        pstmt.setString(3, car.getTransmission().toString());
        pstmt.setInt(4, car.getHorsepower());
        pstmt.setString(5, car.getEngineType().toString());
        pstmt.setString(6, car.getDrivetrain().toString());
        pstmt.setString(7, car.getCarClass().toString());
        pstmt.setInt(8, car.getKmLimit());
        pstmt.setInt(9, car.getSpeedLimit());
        pstmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM car WHERE idCar=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();

        ProductCRUD p = ProductCRUD.getInstance();
        p.delete(id);
    }

    @Override
    public void update(int id, Car updatedCar) throws SQLException {
        // nu
    }
}
