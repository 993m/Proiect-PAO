package service;

import model.Address;
import model.Card;
import model.Rental;
import model.User;
import model.option.RentalOption;
import model.product.Bicycle;
import model.product.Car;
import model.product.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static model.Card.readNewCard;


public class Service {
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Address> locations = new ArrayList<Address>();
    private ArrayList<User> users = new ArrayList<User>();
    private User user;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Address> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Address> locations) {
        this.locations = locations;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers (ArrayList<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void showProducts(){
        if(products.size() == 0){
            System.out.println("There are no products.");
        }
        else{
            System.out.println(products);
        }
    }

    Date readDate() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter date (format: " + dateFormat + "): ");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }

    int getProductType(){
        Scanner sc = new Scanner(System.in);
        int productType;
        System.out.println("What do you want to rent? (Enter 1 or 2)\n 1. Car\n 2. Bicyle");
        productType = sc.nextInt(); // 1 - Car, 2 - Bicycle
        sc.nextLine();
        while(productType != 1 && productType != 2) {
            System.out.println("Please enter a valid value!");
            System.out.println("What do you want to rent? (Enter 1 or 2)\n 1. Car\n 2. Bicyle");
            productType = sc.nextInt(); // 1 - Car, 2 - Bicycle
            sc.nextLine();
        }
        return productType;
    }

    Date getStartDate() {
        Date startDate=null;
        while(startDate==null)
            try{
                System.out.println("What is the rental start date?");
                startDate = readDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format (example: 16/01/2002): ");
            }
        return startDate;
    }

    Date getEndDate() {
        Date endDate=null;
        while(endDate==null)
            try{
                System.out.println("What is the rental end date?");
                endDate = readDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format (example: 16/01/2002): ");
            }
        return endDate;
    }

    public void getRentableProducts(){
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        int productType = getProductType();
        showRentableProducts(startDate, endDate, productType);
    }

    public ArrayList<Product>  showRentableProducts(Date startDate, Date endDate, int productType){
        ArrayList<Product> products = new ArrayList<Product>();
        System.out.println("These are the products available for rental for the given dates:\n");
        int k=0;
        if(productType==1)
        {
            for(Product p: products)
                if(p instanceof Car && p.isRentable(startDate, endDate))
                {
                    System.out.println((++k) + ". " + p.toString());
                    products.add(p);
                }
        }
        else
        {
            for(Product p: products)
                if(p instanceof Bicycle && p.isRentable(startDate, endDate))
                {
                    System.out.println((++k) + ". " + p.toString());
                    products.add(p);
                }
        }

        if(k==0){
            System.out.println("Actually there are no products available for the given dates sorry.");
        }

        return products;
    }

    public void showUsers(){
        if(users.size() == 0) {
            System.out.println("There are no users.");
        }
        else {
            System.out.println(users);
        }
    }

    public User getUserByUsername(String username){
        for(User user: users)
            if(user.getUsername().equals(username)) {
                return user;
            }
        return null;
    }

    public void runStartMenu(){
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println("1. Log in.");
            System.out.println("2. Sign up.");
            System.out.println("3. View the products.");
            System.out.println("4. Exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    logInUser();
                }
                case 2 -> {
                    signUpUser();
                }
                case 3 -> {
                    showProducts();
                }
                case 4 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }


    public void runUserMenu(User user){
        this.user = user;

        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println("1. View all the products.");
            System.out.println("2. View the available products in a given period of time .");
            System.out.println("3. Rent a product.");
            System.out.println("4. Manage your cards.");
            System.out.println("5. View your rental history.");
            System.out.println("6. Cancel a rental.");
            System.out.println("7. Exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    showProducts();
                }
                case 2 -> {
                    getRentableProducts();
                }
                case 3 -> {
                    createRental();
                }
                case 4 -> {
                    runCardsManagementMenu();
                }
                case 5 -> {
                    showUserRentalHistory();
                }
                case 6 -> {
                    cancelRental();
                }
                case 7 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }

    public void runCardsManagementMenu(){
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println("1. View your cards.");
            System.out.println("2. Add a new card.");
            System.out.println("3. Delete a card.");
            System.out.println("4. Exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    showUserCards();
                }
                case 2 -> {
                    addUserCard();
                }
                case 3 -> {
                    deleteUserCard();
                }
                case 4 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }

    //-------------------------------------------------------- user services:

    private void cancelRental() {
        user.cancelRental();
    }

    private void addUserCard(){
        user.addCard(readNewCard());
    }

    private void deleteUserCard(){
        user.deleteCard(user.selectCard());
    }

    public void showUser(){
        if(user == null) {
            System.out.println("There is no logged in user.");
        }
        System.out.println(user);
    }

    private void showUserCards(){
        user.showCards();
    }

    private void showUserRentalHistory(){
        user.showRentalHistory();
    }

    private void signUpUser(){
        Scanner sc = new Scanner(System.in);
        String username = null;
        while(username == null){
            System.out.println("Username: ");
            username = sc.nextLine();
            for(User user: users)
                if(Objects.equals(user.getUsername(), username))
                {
                    System.out.println("This username is taken!");
                    username = null;
                    break;
                }
        }

        // password

        System.out.println("First name: ");
        String firstName = sc.nextLine();

        System.out.println("Last name: ");
        String lastName = sc.nextLine();

        System.out.println("Email: ");
        String email = sc.nextLine();
        while(!email.matches("^(.+)@(.+)$")){
            System.out.println("This is not a valid email address!");
            System.out.println("Email: ");
            email = sc.nextLine();
        }

        System.out.println("You are successfully signed up!\n");
        User user = new User(username, firstName, lastName, email);
        this.users.add(user);
        showUsers();
        runUserMenu(user);
    }

    private void logInUser(){
        Scanner sc = new Scanner(System.in);
        User user = null;
        while(user == null){
            System.out.println("Enter your username: ");
            String username = sc.nextLine();
            user = getUserByUsername(username);
            if(user == null){
                System.out.println("This username does not exist!");
            }
        }

        System.out.println("You are successfully logged in!\n");
        runUserMenu(user);
    }

    ///////////

    private Product selectProduct(ArrayList<Product> products) {
        if(products.size()==0) return null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the product you want to rent: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= products.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the product you want to rent: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return products.get(index);
    }

    private void showLocations(){
        for(int i=0; i<locations.size(); i++){
            System.out.println((i+1) + ". " + locations.get(i));
        }
    }

    private Address selectPickUpAddress(){
        showLocations();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the pick up address: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= locations.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the pick up address: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return locations.get(index);
    }

    private Address selectReturnAddress(){
        showLocations();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the return address: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= locations.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the return address: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return locations.get(index);
    }

    private RentalOption selectOptionToAdd(Product product){
        System.out.println("These are the extra options available for renting this product: ");
        product.showOptions();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the option you want to add to the rental: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= product.getOptions().size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the option you want to add to the rental: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return product.getOptions().get(index);
    }

    private RentalOption selectOptionToRemove(Rental rental){
        System.out.println("These are the chosen extra options for this rental: ");
        rental.showRentalOptions();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the option you want to remove: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= rental.getRentalOptions().size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the option you want to remove: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return rental.getRentalOptions().get(index);
    }

    private Card managePayment(){
        System.out.println("It's time to select the payment method!");
        System.out.println("1. Cash.");
        System.out.println("2. Card.");
        System.out.print("Choose an option: ");

        Scanner sc = new Scanner(System.in);
        int option;
        option = sc.nextInt();
        sc.nextLine();

        if(option == 1) return null;

        Card card = null;
        if(option == 2)
        do{
            System.out.println("1. See your cards.");
            System.out.println("2. Select an existent card.");
            System.out.println("3. Add a new card.");
            System.out.println("4. Save an exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    user.showCards();
                }
                case 2 -> {
                    card = user.selectCard();
                }
                case 3 -> {
                    user.addCard(readNewCard());
                }
                case 4 -> {
                    if(card == null) {
                        System.out.println("You haven't selected any card (option 2), so you can't complete the payment!");
                    }
                    else{
                        return card;
                    }
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);

        return card;
    }


    private void manageRentalOptions(Rental rental, Product product){
        System.out.println("It's time to select the rental extra options!");

        int option;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("1. View the extra options available for this product.");
            System.out.println("2. View the extra options that have been chosen for this rental.");
            System.out.println("3. Add a rental option.");
            System.out.println("4. Remove rental option.");
            System.out.println("5. Save an exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    product.showOptions();
                }
                case 2 -> {
                    rental.showRentalOptions();
                }
                case 3 -> {
                    rental.addRentalOption(selectOptionToAdd(product));
                }
                case 4 -> {
                    rental.deleteRentalOption(selectOptionToRemove(rental));
                }
                case 5 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);

    }

    private void createRental(){
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        int productType = getProductType();
        Product product = selectProduct(showRentableProducts(startDate, endDate, productType));
        if(product == null) {
            return;
        }
        Address pickUpAddress = selectPickUpAddress();
        Address returnAddress = selectReturnAddress();

        Rental newRental = new Rental(product, this.user, startDate, endDate, pickUpAddress, returnAddress);
        manageRentalOptions(newRental, product);

        managePayment();
    }
}
