package model;

import java.time.DateTimeException;
import java.util.Date;
import java.util.Objects;

public class Card {
    String number;
    Date expDate;

    public Card(String number, Date expDate) {
        this.number = number;
        this.expDate = expDate;
    }

    public Card(Card c) {
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
        return "Card: " +
                "card number='" + number + '\'' +
                ", expiration date=" + expDate + '\n';
    }
}
