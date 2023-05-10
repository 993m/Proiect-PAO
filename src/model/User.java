package model;

import model.option.RentalOption;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Rental> rentalHistroy = new ArrayList<Rental>();
    private ArrayList<Card> cards = new ArrayList<Card>();

    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(@NotNull User usr) {
        this.username = usr.username;
        this.firstName = usr.firstName;
        this.lastName = usr.lastName;
        this.email = usr.email;

        rentalHistroy = new ArrayList<Rental>();
        for(Rental r: usr.getRentalHistroy()){
            this.rentalHistroy.add(new Rental(r));
        }

        cards = new ArrayList<Card>();
        for(Card c: usr.getCards()){
            this.cards.add(new Card(c));
        }
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

    public ArrayList<Rental> getRentalHistroy() {
        return rentalHistroy;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setRentalHistroy(ArrayList<Rental> rentalHistroy) {
        this.rentalHistroy = rentalHistroy;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card c){
        cards.add(c);
        System.out.println("The card was added.");
        showCards();
    }

    public ArrayList<Rental> getCancelableRentals(){
        ArrayList<Rental> rentals = new ArrayList<Rental>();
        Date currentDate = new Date();
        for(Rental r: rentalHistroy){
            if(r.getStartDate().after(currentDate)){
                rentals.add(r);
            }
        }
        return rentals;
    }

    public Rental selectRentalToCancel(){
        ArrayList<Rental> rentals = getCancelableRentals();

        // show rentals
        System.out.println("These are your current rentals: ");
        for(int i=0; i<rentals.size(); i++)
            System.out.println((i+1) + ". " + rentals.get(i));

        //select rentals
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the rental you want to cancel: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= rentals.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the rental you want to cancel: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return rentals.get(index);
    }

    public void cancelRental(){
        Rental r = selectRentalToCancel();
        rentalHistroy.remove(r);
        r.getProduct().getRentals().remove(r);
    }

    public Card selectCard(){
        showCards();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the card you want to delete: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= cards.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the card you want to delete: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return cards.get(index);
    }

    public void addRentalToHistory(Rental r){
        rentalHistroy.add(r);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", rentalHistroy=" + rentalHistroy +
                ", cards=" + cards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(rentalHistroy, user.rentalHistroy) && Objects.equals(cards, user.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, rentalHistroy, cards);
    }

    public void showRentalHistory(){
        System.out.println(username + "'s rental history: ");
        if(rentalHistroy.size()==0) {
            System.out.println("There is no rental history for this user.");
        }
        else {
            System.out.println(rentalHistroy);
        }
    }

    public void showCards(){
        System.out.println(username + "'s list of cards: ");
        if(cards.size() == 0) {
            System.out.println("The user does not have any card.");
        }
        else{
            for(int i=0; i<cards.size(); i++)
                System.out.println((i+1) + ". " + cards.get(i));
        }
    }

    public void deleteCard(Card card) {
        cards.remove(card);
        System.out.println("The card was removed.");
        showCards();
    }
}
