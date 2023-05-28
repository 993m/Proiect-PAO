package model;

import databaseAccess.CardCRUD;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Card {
    private int id;
    private String number;
    private Date expDate;
    private User user;


    private static final CardCRUD cardCRUD = CardCRUD.getInstance();


    public Card(User user, String number, Date expDate) {
        this.number = number;
        this.expDate = expDate;
        this.user = user;
    }

    public Card(@NotNull Card c) {
        this.id = c.id;
        this.number = c.number;
        this.expDate = c.expDate;
        this.user = c.user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }



    public static Date readExpDate() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter expiration date (format: " + dateFormat + "): ");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }

    static public @NotNull Card readNewCard(@NotNull User user){
        Scanner sc = new Scanner(System.in);
        String number;
        Date expDate = null;

        System.out.println("Enter the card number: ");
        number = sc.nextLine();

        while(expDate==null)
            try{
                expDate = readExpDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format (example: 16/01/2002): ");
            }

        return new Card(user, number, expDate);
    }

    public void updateCard(Card c) throws SQLException {
        cardCRUD.update(id, c);
        System.out.println("The card was updated.");
    }

    public static void addCard(Card c) throws SQLException {
        cardCRUD.add(c);
        System.out.println("The card was added.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number.equals(card.number) && expDate.equals(card.expDate) && user.equals(card.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, expDate, user);
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", expDate=" + expDate +
                '}';
    }
}
