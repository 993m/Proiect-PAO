package databaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection conn;
    private static DatabaseConnection dbConnection;

    private DatabaseConnection() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/proiect_pao";
            String username = "root";
            String password = "MySQLRootPa55";

            conn = DriverManager.getConnection(url, username, password);
        }
        catch (Exception exception){
            System.out.println("Failed to connect to database: " + exception.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException{
        if(dbConnection == null)
            dbConnection = new DatabaseConnection();
        return conn;
    }
}
