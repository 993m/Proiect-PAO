package service;

import model.Address;
import model.Rental;
import model.User;
import model.option.RentalOption;
import model.product.Bicycle;
import model.product.Car;
import model.product.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Service {
    Product[] products;
    Address[] locations;
    User[] users;
    User user;
    Scanner sc = new Scanner(System.in);

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public Address[] getLocations() {
        return locations;
    }

    public void setLocations(Address[] locations) {
        this.locations = locations;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void showProducts(){
        for(int i=0; i<products.length; i++)
            System.out.println(i+1 + ". " + products[i].toString() + '\n');

    }

    public void showRentableProducts(Date startDate, Date endDate){
        System.out.println("These are the products available for rental for the given dates:\n");
        int productType = getProductType();

        if(productType==1)
        {
            for(int i=0; i<products.length; i++)
                if(products[i] instanceof Car && products[i].isRentable(startDate, endDate))
                    System.out.println(i+1 + ". " + products[i].toString() + '\n');
        }
        else{
            if(productType==1)
            {
                for(int i=0; i<products.length; i++)
                    if(products[i] instanceof Bicycle && products[i].isRentable(startDate, endDate))
                        System.out.println(i+1 + ". " + products[i].toString() + '\n');
            }
        }
    }


    public void addProduct(Product p){
        int n=0;
        if(products != null) n = this.products.length;
        Product[] newProducts = new Product[n+1];
        System.arraycopy(this.products, 0, newProducts, 0, n);
        newProducts[n+1] = p;
        this.products = new Product[n+1];
        System.arraycopy(newProducts, 0, this.products, 0, n+1);
    }

    //IMPLEMENTARE
    public Product readNewProduct(){
        return null;
    }

    public void addUser(User u){
        int n=0;
        if(users != null)  n = this.users.length;
        User[] newUsers = new User[n+1];
        System.arraycopy(this.users, 0, newUsers, 0, n);
        newUsers[n+1] = u;
        this.users = new User[n+1];
        System.arraycopy(newUsers, 0, this.users, 0, n+1);
    }

    public User readNewUser(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: \n");
        String username = sc.nextLine();
        System.out.println("First name: \n");
        String firstName = sc.nextLine();;
        System.out.println("Last name: \n");
        String lastName = sc.nextLine();;
        System.out.println("Email: \n");
        String email = sc.nextLine();

        User user = new User(username, firstName, lastName, email);
        this.addUser(user);
        return user;
    }

    public void addAddress(Address a){
        int n=0;
        if(locations!=null)  n = this.locations.length;
        Address[] newLocations = new Address[n+1];
        System.arraycopy(this.locations, 0, newLocations, 0, n);
        newLocations[n+1] = a;
        this.locations = new Address[n+1];
        System.arraycopy(newLocations, 0, this.locations, 0, n+1);
    }

    //IMPLEMENTARE
    public Address readNewAddress(){
        return null;
    }

    public void showUsers(){
        for(User u: users)  u.toString();
    }

    public void showLocations(){
        for(Address l: locations)  l.toString();
    }

    public User getUserByUsername(String username){
        for(User user: users)
            if(user.getUsername() == username) return user;
        return null;
    }








    //-------------------------------------------------------- user services:

    public void showUser(){
        user.toString();
    }

    public void showUserCards(){
        user.showCards();
    }

    public void showUserRentalHistory(){
        user.showRentalHistory();
    }





    //VALIDARE INPUT
    int getProductType(){
        System.out.println("What do you want to rent? (Enter 1 or 2)\n 1. Car\n 2. Bicyle\n");
        return sc.nextInt();  // 1 - Car, 2 - Bicycle
    }

    Date readStartDate() throws ParseException {
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter rental start date (format: " + dateFormat + "):");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }

    Date readEndDate() throws ParseException {
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter rental end date (format: " + dateFormat + "):");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }



    public void createRental(){
        Date startDate=null, endDate=null;
        while(startDate!=null)
            try{
                startDate = readStartDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format:");
            } ;

        while(endDate!=null)
            try{
                endDate = readEndDate();
            }
            catch (ParseException e) {
                System.out.println("Please respect the date format:");
            } ;

        showRentableProducts(startDate, endDate);



        System.out.println("Enter the number of the product you would like to rent:\n");
        int index = sc.nextInt();
        Product product = products[index-1];

        System.out.println("Choose pick-up and drop-off locations:\n");
        showLocations();
        System.out.println("Enter the number of the pick-up address: ");
        int pickUpAddressIndex = sc.nextInt() -1;
        System.out.println("Enter the number of the drop-off address: ");
        int returnAddressIndex = sc.nextInt() -1;


        Rental newRental = new Rental(product, user, startDate, endDate, locations[pickUpAddressIndex-1], locations[returnAddressIndex - 1]);


        if(product.getOptions()!=null && product.getOptions().length!=0){
            System.out.println("The extra options for this product are:\n");
            product.showOptions();
            System.out.println("Do you want to add an option? (yes/no)\n");
            while(sc.nextLine() == "yes"){
                System.out.println("Enter the number of the option: ");
                index = sc.nextInt()-1;
                newRental.addRentalOption(product.getOptions()[index]);
                System.out.println("The option has been added. Do you want to add another? (yes/no)\n");
            }
        }


        // implementare selectare metoda de plata/adaugare card

        newRental.updateTotalPrice();
        System.out.println("The total price is: " + newRental.getTotalPrice());
    }





}
