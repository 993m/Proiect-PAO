package model;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Card {
    private String number;
    private Date expDate;

    public Card(String number, Date expDate) {
        this.number = number;
        this.expDate = expDate;
    }

    public Card(@NotNull Card c) {
        this.number = c.number;
        this.expDate = c.expDate;
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

    static Date readExpDate() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter expiration date (format: " + dateFormat + "): ");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }

    static public Card readNewCard(){
        String number;
        Date expDate = null;
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the card number: ");
        number = sc.nextLine();
        while(expDate==null)
            try{
                expDate = readExpDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format (example: 16/01/2002): ");
            }

        return new Card(number, expDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(number, card.number) && Objects.equals(expDate, card.expDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, expDate);
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", expDate=" + expDate +
                '}';
    }
}
