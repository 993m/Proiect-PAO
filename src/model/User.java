package model;


import databaseAccess.CardCRUD;
import databaseAccess.RentalCRUD;
import databaseAccess.UserCRUD;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private static final UserCRUD userCRUD = UserCRUD.getInstance();
    private static final CardCRUD cardCRUD = CardCRUD.getInstance();
    private static final RentalCRUD rentalCRUD = RentalCRUD.getInstance();

    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(@NotNull User user){
        this.id = user.id;
        this.username = user.username;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public static String readUsername() throws SQLException{
        Scanner sc = new Scanner(System.in);
        String username = null;
        ArrayList<User> users = userCRUD.getAll();

        while(username == null) {
            System.out.println("Username: ");
            username = sc.nextLine();
            for (User u : users)
                if (Objects.equals(u.getUsername(), username)) {
                    System.out.println("This username is taken!");
                    username = null;
                    break;
                }
        }

        return username;
    }
    public static String readEmail() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String email = null;
        ArrayList<User> users = userCRUD.getAll();

        while(email == null){
            System.out.println("Email: ");
            email = sc.nextLine();

            if(!email.matches("^(.+)@(.+)$")){
                System.out.println("This is not a valid email address!");
                System.out.println("Email: ");
                email = sc.nextLine();
            }
            else for (User u : users)
                if (Objects.equals(u.getEmail(), email)) {
                    System.out.println("This email already exist!");
                    email = null;
                    break;
                }
        }

        return email;
    }


    public ArrayList<Rental> getRentalHistory() throws SQLException{
        return rentalCRUD.getAllForUser(this);
    }

    public void showRentalHistory() throws SQLException {
        System.out.println(username + "'s rental history: ");
        ArrayList<Rental> rentalHistory = getRentalHistory();
        if(rentalHistory.size()==0) {
            System.out.println("There is no rental history for this user.");
        }
        else {
            for(Rental r: rentalHistory){
                System.out.println(r);
            }
        }
    }

    public ArrayList<Rental> getCancelableRentals() throws SQLException{
        ArrayList<Rental> rentals = new ArrayList<>(0);
        Date currentDate = new Date();
        for(Rental r: getRentalHistory()){
            if(r.getStartDate().after(currentDate)){
                rentals.add(r);
            }
        }
        return rentals;
    }

    public Rental selectRentalToCancel() throws SQLException {
        ArrayList<Rental> rentals = getCancelableRentals();
        if(rentals.size()==0) {
            System.out.println("You don't have rentals.");
            return null;
        }

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



    public Rental selectRentalToEdit() throws SQLException {
        ArrayList<Rental> rentals = getCancelableRentals();
        if(rentals.size()==0) {
            System.out.println("You don't have rentals.");
            return null;
        }

        // show rentals
        System.out.println("These are your current rentals: ");
        for(int i=0; i<rentals.size(); i++)
            System.out.println((i+1) + ". " + rentals.get(i));

        //select rentals
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the rental you want to edit: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= rentals.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the rental you want to edit: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return rentals.get(index);
    }

    public void cancelRental() throws SQLException {
        Rental r = selectRentalToCancel();
        if(r == null) return;
        rentalCRUD.delete(r.getId());
        System.out.println("The rental was canceled.");
    }

    public ArrayList<Card> getCards() throws SQLException{
        return cardCRUD.getAll(this);
    }

    public void showCards() throws SQLException{
        ArrayList<Card> cards = getCards();

        if(cards.size() == 0) {
            System.out.println("The user does not have any card.");
        }
        else{
            System.out.println(username + "'s list of cards: ");
            for(int i=0; i<cards.size(); i++)
                System.out.println((i+1) + ". " + cards.get(i));
        }
    }

    public Card selectCard() throws SQLException{
        Scanner sc = new Scanner(System.in);

        showCards();
        ArrayList<Card> cards = getCards();
        if(cards.size()==0){
            return null;
        }

        System.out.println("Enter the index of the card you want to select: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= cards.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the card you want to select: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return cards.get(index);
    }

    public void deleteCard() throws SQLException {
        Card card = selectCard();
        if(card == null) return;
        cardCRUD.delete(card.getId());
        System.out.println("The card was removed.");
        //showCards();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email);
    }

}
