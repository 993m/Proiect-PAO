package databaseAccess;

import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CRUD <T>{
    public T mapResultSet(@NotNull ResultSet rs) throws SQLException;
    public void add(@NotNull T obj) throws SQLException;
    public void delete(int id) throws SQLException;
    public T get(int id) throws SQLException;
    public void update(int id, T updatedObj) throws SQLException;
    public ArrayList<T> getAll() throws SQLException;
}
