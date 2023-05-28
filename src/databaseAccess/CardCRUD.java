package databaseAccess;

import model.Card;
import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardCRUD implements CRUD<Card> {
    private static CardCRUD instance = null;
    private CardCRUD(){};
    public static CardCRUD getInstance(){
        if(instance == null)
            instance =  new CardCRUD();
        return instance;
    }

    @Override
    public void add(@NotNull Card card) throws SQLException {
        String sqlQuery = "INSERT INTO card (number, expDate, id_user) VALUES (?,?,?)";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, card.getNumber());
        pstmt.setDate(2, new java.sql.Date(card.getExpDate().getTime()));
        pstmt.setInt(3, card.getUser().getId());
        pstmt.executeUpdate();

        sqlQuery = "SELECT LAST_INSERT_ID()";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        rs.next();
        card.setId(rs.getInt(1));
    }

    @Override
    public void delete(int id) throws SQLException {
        String sqlQuery = "DELETE FROM card WHERE idCard=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    @Override
    public Card get(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM card WHERE idCard=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return mapResultSet(rs);
    }

    @Override
    public void update(int id, @NotNull Card updatedCard) throws SQLException {
        String sqlQuery = "UPDATE card SET number=?, expDate=? WHERE idCard=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setString(1, updatedCard.getNumber());
        pstmt.setDate(2, new java.sql.Date(updatedCard.getExpDate().getTime()));
        pstmt.setInt(3, id);
        pstmt.executeUpdate();
    }

    @Override
    public ArrayList<Card> getAll() throws SQLException {
        ArrayList<Card> cards = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM card";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sqlQuery);
        while(rs.next())
            cards.add(mapResultSet(rs));
        return cards;
    }

    public ArrayList<Card> getAll(@NotNull User user) throws SQLException {
        ArrayList<Card> cards = new ArrayList<>(0);
        String sqlQuery = "SELECT * FROM card WHERE id_user=?";
        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sqlQuery);
        pstmt.setInt(1, user.getId());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
            cards.add(mapResultSet(rs));
        return cards;
    }

    @Override
    public Card mapResultSet(@NotNull ResultSet rs) throws SQLException {
        UserCRUD u = UserCRUD.getInstance();
        Card card = new Card(u.get(rs.getInt("id_user")), rs.getString("number"), rs.getDate("expDate"));
        card.setId(rs.getInt("idCard"));
        return card;
    }


}
