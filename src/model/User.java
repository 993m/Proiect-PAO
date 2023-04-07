package model;

import model.option.RentalOption;
import model.product.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public class User {
    String username;
    String firstName;
    String lastName;
    String email;
    Rental[] rentalHistroy;
    Card[] cards;


    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(User usr) {
        this.username = usr.username;
        this.firstName = usr.firstName;
        this.lastName = usr.lastName;
        this.email = usr.email;

        rentalHistroy = new Rental[usr.rentalHistroy.length];
        for(int  i = 0; i < usr.rentalHistroy.length; i++)
            this.rentalHistroy[i] = usr.rentalHistroy[i];

        cards = new Card[usr.cards.length];
        for(int  i = 0; i < usr.cards.length; i++)
            this.cards[i] = usr.cards[i];
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rental[] getRentalHistroy() {
        return rentalHistroy;
    }

    public void setRentalHistroy(Rental[] rentalHistroy) {
        this.rentalHistroy = rentalHistroy;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public void addCard(Card c){
        int n=0;
        if(cards!=null) n = cards.length;

        Card[] newCards = new Card[n + 1];

        System.arraycopy(this.cards, 0, newCards, 0, n);

        newCards[n + 1] = c;

        this.cards = new Card[n + 1];
        System.arraycopy(newCards, 0, this.cards, 0, n + 1);

    }

    public void addRentalToHistory(Rental r){
        int n=0;
        if(rentalHistroy!=null)  n = rentalHistroy.length;

        Rental[] newRentals = new Rental[n + 1];

        System.arraycopy(this.rentalHistroy, 0, newRentals, 0, n);

        newRentals[n + 1] = r;

        this.rentalHistroy = new Rental[n + 1];
        System.arraycopy(newRentals, 0, this.rentalHistroy, 0, n + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Arrays.equals(rentalHistroy, user.rentalHistroy) && Arrays.equals(cards, user.cards);
    }

    @Override
    public String toString() {
        return "User: " + username + " " + firstName + " " + lastName + ", " + email + "\n";
    }

    public void showRentalHistory(){
        for(Rental rental: this.rentalHistroy)
            rental.toString();
    }

    public void showCards(){
        for(Card card: this.cards)
            card.toString();
    }

}
