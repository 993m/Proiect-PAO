package service;

import databaseAccess.*;
import model.Address;
import model.Card;
import model.Rental;
import model.User;
import model.option.AddressRentalOption;
import model.option.RentalOption;
import model.product.Bicycle;
import model.product.Car;
import model.product.Product;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static model.Card.readNewCard;
import static model.User.readEmail;
import static model.User.readUsername;


public class ShopService {
    private static ShopService instance = null;
    private static Audit audit = null;
    private ShopService(){
        try {
            audit = Audit.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static ShopService getInstance(){
        if(instance == null){
            instance = new ShopService();
        }
        return instance;
    }

    private static final UserCRUD userCRUD = UserCRUD.getInstance();
    private static final  CarCRUD carCRUD = CarCRUD.getInstance();
    private static final  BicycleCRUD bicycleCRUD = BicycleCRUD.getInstance();
    private static final  RentalCRUD rentalCRUD = RentalCRUD.getInstance();
    private static final  AddressCRUD addressCRUD = AddressCRUD.getInstance();


    private static void showProducts() throws SQLException, IOException {
        int type = getProductType();
        PriorityQueue<Product> products = new PriorityQueue<>(1, (Product p1, Product p2) -> p1.compareTo(p2));
        if(type == 1) {
            products.addAll(carCRUD.getAll());
        }
        else {
            products.addAll(bicycleCRUD.getAll());
        }

        if(products.size() == 0){
            System.out.println("There are no products.");
        }
        else{
            for(int i=0; i<products.size(); i++){
                System.out.println((i+1) + ". " + products.poll());
            }
        }

        audit.log("Show products.");
    }
    private static void showLocations() throws SQLException{
        ArrayList<Address> locations = addressCRUD.getAll();
        if(locations.size() == 0){
            System.out.println("There are no locations.");
        }
        else{
            for(int i=0; i<locations.size(); i++){
                System.out.println((i+1) + ". " + locations.get(i));
            }
        }
    }

    public void runStartMenu() throws SQLException, ParseException, IOException {
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
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

    private void signUpUser() throws SQLException, ParseException, IOException {
        Scanner sc = new Scanner(System.in);

        String username = readUsername();

        // password

        System.out.println("First name: ");
        String firstName = sc.nextLine();

        System.out.println("Last name: ");
        String lastName = sc.nextLine();

        String email = readEmail();

        User user = new User(username, firstName, lastName, email);
        userCRUD.add(user);
        System.out.println("You are successfully signed up!\n");
        audit.log("Signed up user.");
        runUserMenu(user);
    }

    private void logInUser() throws SQLException, ParseException, IOException {
        Scanner sc = new Scanner(System.in);
        User user = null;
        while(user == null){
            System.out.println("Enter your username: ");
            String username = sc.nextLine();

            user = userCRUD.getUserByUsername(username);

        }

        System.out.println("You are successfully logged in!\n");
        audit.log("Logged in user.");
        runUserMenu(user);
    }

    private void runUserMenu(User user) throws SQLException, ParseException, IOException {
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
            System.out.println("1. View all the products.");
            System.out.println("2. View the available products in a given period of time .");
            System.out.println("-------");
            System.out.println("3. Rent a product.");
            System.out.println("4. Edit a rental.");
            System.out.println("5. Cancel a rental.");
            System.out.println("6. View your rental history.");
            System.out.println("-------");
            System.out.println("7. Manage your cards.");
            System.out.println("-------");
            System.out.println("8. Manage your profile.");
            System.out.println("-------");
            System.out.println("9. Exit.\n");
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
                    createRental(user);
                }
                case 4 -> {
                    editRental(user);
                }
                case 5 -> {
                    cancelRental(user);
                }
                case 6 -> {
                    showRentalHistory(user);
                }
                case 7 -> {
                    runCardsManagementMenu(user);
                }
                case 8 -> {
                    if(runProfileManagementMenu(user) == null){
                        return;
                    }
                }
                case 9 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }



    // USER
    private void updateUser(@NotNull User user) throws SQLException, IOException {
        userCRUD.update(user.getId(), runEditProfileMenu(user));
        audit.log("Update user profile.");
    }

    private User runEditProfileMenu(User user) throws SQLException{
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
            System.out.println(user);
            System.out.println();
            System.out.println("1. Change username.");
            System.out.println("2. Change first name.");
            System.out.println("3. Change last name.");
            System.out.println("4. Change email.");
            System.out.println("5. Save and exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    user.setUsername(readUsername());
                }
                case 2 -> {
                    System.out.println("First name: ");
                    user.setFirstName(sc.nextLine());
                }
                case 3 -> {
                    System.out.println("Last name: ");
                    user.setLastName(sc.nextLine());
                }
                case 4 -> {
                    user.setEmail(readEmail());
                }
                case 5 -> {
                    return user;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }

    private void showRentalHistory(@NotNull User user) throws SQLException, IOException {
        user.showRentalHistory();
        audit.log("Show rental history for user.");
    }

    private void editRental(@NotNull User user) throws SQLException, IOException {
        Rental rental = user.selectRentalToEdit();
        if(rental != null) {
            runEditRentalMenu(rental);
        }
    }

    private void cancelRental(@NotNull User user) throws SQLException, IOException {
        user.cancelRental();
        audit.log("Cancel rental.");
    }

    private void showCards(@NotNull User user) throws SQLException, IOException {
        user.showCards();
        audit.log("Show cards for user.");
    }

    private void addCard(@NotNull User user) throws SQLException, IOException {
        Card.addCard(readNewCard(user));
        audit.log("Add card for user.");
    }

    private void deleteCard(@NotNull User user) throws SQLException, IOException {
        user.deleteCard();
        audit.log("Delete a user card.");
    }

    private void deleteUser(@NotNull User user) throws SQLException, IOException {
        userCRUD.delete(user.getId());
        audit.log("Delete user account.");
    }

    private void updateCard(@NotNull User user) throws SQLException, ParseException, IOException {
        audit.log("Update user card.");
        Card card = user.selectCard();
        if(card == null) return;
        card.updateCard(runEditCardMenu(card));
    }

    private void showUserProfile(User user) throws IOException {
        System.out.println(user);
        audit.log("Show user profile.");
    }
    private @Nullable User runProfileManagementMenu(User user) throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
            System.out.println("1. View your profile.");
            System.out.println("2. Edit your profile.");
            System.out.println("3. Delete your account.");
            System.out.println("4. Exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    showUserProfile(user);
                }
                case 2 -> {
                    updateUser(user);
                }
                case 3 -> {
                    deleteUser(user);
                    return null;
                }
                case 4 -> {
                    return user;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }

    private void showUserCards(@NotNull User user) throws SQLException {
        user.showCards();
    }

    private Card selectUserCard(@NotNull User user) throws SQLException {
        return user.selectCard();
    }



    // CARD
    private void runCardsManagementMenu(User user) throws SQLException, ParseException, IOException {
        System.out.println();
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println("1. View your cards.");
            System.out.println("2. Edit a card.");
            System.out.println("3. Add a new card.");
            System.out.println("4. Delete a card.");
            System.out.println("5. Exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    showCards(user);
                }
                case 2 -> {
                    updateCard(user);
                }
                case 3 -> {
                    addCard(user);
                }
                case 4 -> {
                    deleteCard(user);
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

    private static Card runEditCardMenu(Card card) throws ParseException {
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
            System.out.println(card);
            System.out.println();
            System.out.println("1. Change number.");
            System.out.println("2. Change expiration date.");
            System.out.println("3. Save and exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    card.setNumber(sc.nextLine());
                }
                case 2 -> {
                    card.setExpDate(Card.readExpDate());
                }
                case 3 -> {
                    return card;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }





    // RENTAL
    private Product getProduct(java.util.Date startDate, java.util.Date endDate) throws SQLException, IOException {
        return selectProduct(showRentableProducts(startDate, endDate, getProductType()));

    }

    private static int getProductType(){
        Scanner sc = new Scanner(System.in);
        int productType;
        System.out.println("What do you want to see? (Enter 1 or 2)\n 1. Car\n 2. Bicyle");
        productType = sc.nextInt(); // 1 - Car, 2 - Bicycle
        sc.nextLine();

        while(productType != 1 && productType != 2) {
            System.out.println("Please enter a valid value!");
            System.out.println("What do you want to see? (Enter 1 or 2)\n 1. Car\n 2. Bicyle");
            productType = sc.nextInt(); // 1 - Car, 2 - Bicycle
            sc.nextLine();
        }
        return productType;
    }

    private void getRentableProducts() throws SQLException, IOException {
        ArrayList<Date> dates = getRentalPeriod();
        showRentableProducts(dates.get(0), dates.get(1), getProductType());
        audit.log("Show available products for a given period of time.");
    }

    private @NotNull ArrayList<Product>  showRentableProducts(Date startDate, Date endDate, int productType) throws SQLException{
        //ArrayList<Product> products = productCRUD.getAll();

        System.out.println("These are the products available for rental for the given dates:\n");

        ArrayList<Product> products = new ArrayList<>(0);
        int k=0;

        if(productType==1)
        {
            ArrayList<Car> cars = carCRUD.getAll();
            for(Car c: cars)
                if(c.isRentable(startDate, endDate))
                {
                    System.out.println((++k) + ". " + c);
                    products.add(c);
                }
        }
        else
        {
            ArrayList<Bicycle> bikes = bicycleCRUD.getAll();
            for(Bicycle b: bikes)
                if(b.isRentable(startDate, endDate))
                {
                    System.out.println((++k) + ". " + b);
                    products.add(b);
                }
        }

        if(k==0){
            System.out.println("Actually there are no products available for the given dates sorry.");
        }

        return products;
    }

    private @Nullable Product selectProduct(@NotNull ArrayList<Product> products) {
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

    private java.util.Date readDate() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String dateFormat = "dd/MM/yyyy";
        System.out.print("Enter date (format: " + dateFormat + "): ");
        return new SimpleDateFormat(dateFormat).parse(sc.next());
    }

    private java.util.Date getStartDate() {
        java.util.Date startDate=null;
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

    private java.util.Date getEndDate() {
        java.util.Date endDate=null;
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

    private @NotNull ArrayList<java.util.Date> getRentalPeriod(){
        System.out.println("Setelct the rental period: ");
        java.util.Date startDate = getStartDate();
        java.util.Date endDate = getEndDate();

        while(startDate.after(endDate)){
            System.out.println("Start date can't be after end date.");
            System.out.println("Select another period: ");
            startDate = getStartDate();
            endDate = getEndDate();
        }

        ArrayList<java.util.Date> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        return dates;
    }


    private void createRental(User user) throws SQLException, IOException {
        ArrayList<Date> dates = getRentalPeriod();
        java.util.Date startDate = dates.get(0);
        java.util.Date endDate = dates.get(1);


        Product product = getProduct(startDate, endDate);
        if(product == null) return;

        Address pickUpAddress = selectPickUpAddress();
        Address returnAddress = selectReturnAddress();

        Rental newRental = new Rental(product, user, startDate, endDate, pickUpAddress, returnAddress);


        rentalCRUD.add(newRental);
        System.out.println("The rental was created.");
        audit.log("Created rental.");

        manageRentalOptions(newRental, product);

        managePayment(user);
    }



    private Address selectPickUpAddress() throws  SQLException{
        showLocations();
        ArrayList<Address> locations = addressCRUD.getAll();

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

    private Address selectReturnAddress() throws SQLException{
        showLocations();
        ArrayList<Address> locations = addressCRUD.getAll();

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

    private void showProductOptions(@NotNull Product product) throws SQLException, IOException {
        product.showOptions();
        audit.log("View extra options for a product.");
    }
    private void showRentalOptions(@NotNull Rental rental) throws SQLException, IOException {
        rental.showRentalOptions();
        audit.log("View selected options for a rental.");
    }

    private void addRentalOption(Product product, Rental rental) throws SQLException, IOException {
        RentalOption opt = selectOptionToAdd(product);
        if(opt!=null){
            rental.addRentalOption(opt);
        }
        audit.log("Add extra option to a rental.");
    }

    private void editRentalOption(Rental rental) throws SQLException, IOException {
        AddressRentalOption opt = selectOptionToEdit(rental);
        if(opt != null) {
            opt.setAddress(runEditAddressMenu(opt.getAddress()));
        }
        audit.log("Edit rental extra option.");
    }

    private void deleteRentalOption(Rental rental) throws SQLException, IOException {
        RentalOption opt = selectOptionToRemove(rental);
        if(opt!=null){
            rental.deleteRentalOption(opt);
        }
        audit.log("Remove a selected rental option.");
    }

    private void manageRentalOptions(Rental rental, Product product) throws SQLException, IOException {
        System.out.println("It's time to select the rental extra options!");

        int option;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("1. View the extra options available for this product.");
            System.out.println("2. View the extra options that have been chosen for this rental.");
            System.out.println("3. Add a rental option.");
            System.out.println("4. Edit a rental option.");
            System.out.println("5. Remove rental option.");
            System.out.println("6. Save an exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    showProductOptions(product);
                }
                case 2 -> {
                    showRentalOptions(rental);
                }
                case 3 -> {
                    addRentalOption(product, rental);
                }
                case 4 -> {
                    editRentalOption(rental);
                }
                case 5 -> {
                    deleteRentalOption(rental);
                }
                case 6 -> {
                    return;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }

    public Address runEditAddressMenu(Address a) {
        Scanner sc = new Scanner(System.in);
        int option;
        do{
            System.out.println();
            System.out.println(a);
            System.out.println();
            System.out.println("1. Change city.");
            System.out.println("2. Change street name.");
            System.out.println("3. Change street number.");
            System.out.println("4. Save and exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    System.out.println("City: ");
                    a.setCity(sc.nextLine());
                }
                case 2 -> {
                    System.out.println("Street name: ");
                    a.setStreetName(sc.nextLine());
                }
                case 3 -> {
                    System.out.println("Street number: ");
                    a.setStreetNumber(sc.nextInt());
                    sc.nextLine();
                }
                case 4 -> {
                    return a;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);
    }


    private @Nullable RentalOption selectOptionToAdd(@NotNull Product product) throws SQLException {
        System.out.println("These are the extra options available for renting this product: ");
        product.showOptions();
        ArrayList<RentalOption> options = product.getOptions();
        if(options == null) return null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the option you want to add to the rental: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= options.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the option you want to add to the rental: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return options.get(index);
    }

    private @Nullable RentalOption selectOptionToRemove(@NotNull Rental rental) throws SQLException {
        System.out.println("These are the chosen extra options for this rental: ");
        rental.showRentalOptions();
        ArrayList<RentalOption> options = rental.getOptions();
        if(options.size()==0) return null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the option you want to remove: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= options.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the option you want to remove: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return options.get(index);
    }

    private @Nullable AddressRentalOption selectOptionToEdit(@NotNull Rental rental) throws SQLException {
        System.out.println("These are the chosen extra options that you can edit: ");
        rental.showAddressRentalOptions();
        ArrayList<AddressRentalOption> options = rental.getAddressOptions();
        if(options.size()==0) return null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the index of the option you want to edit: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        while(index<0 || index >= options.size()){
            System.out.println("Invalid index entered!");
            System.out.println("Enter the index of the option you want to edit: ");
            index = sc.nextInt() - 1;
            sc.nextLine();
        }

        return options.get(index);
    }

    public Card managePayment(User user) throws SQLException, IOException {
        System.out.println("It's time to select the payment method!");
        System.out.println("1. Cash.");
        System.out.println("2. Card.");
        System.out.print("Choose an option: ");

        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
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
                        showUserCards(user);
                    }
                    case 2 -> {
                        card = selectUserCard(user);
                    }
                    case 3 -> {
                        addCard(user);
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



    private void changeRentalPeriod(@NotNull Rental rental) throws SQLException, IOException {
        ArrayList<java.util.Date> dates = getRentalPeriod();
        Scanner sc = new Scanner(System.in);

        if(!rental.getProduct().isRentable(dates.get(0), dates.get(1))){
            System.out.println("The chosen product is not available in this period.");
            System.out.println("Do you want to keep the selected period? (yes/no) ");
            if(sc.nextLine() == "yes"){
                Product newProduct = getProduct(rental.getStartDate(), rental.getEndDate());
                if(newProduct == null){
                    System.out.println("Failed changing the rental period. Try again: ");
                    changeRentalPeriod(rental);
                }
                else {
                    rental.setStartDate(dates.get(0));
                    rental.setEndDate(dates.get(1));
                    rental.setProduct(newProduct);
                }

            }
        }
        else{
            rental.setStartDate(dates.get(0));
            rental.setEndDate(dates.get(1));
        }

        System.out.println("Changes were successfully saved!");
    }

    private void changeProduct(@NotNull Rental rental) throws SQLException, IOException {
        Product product = getProduct(rental.getStartDate(), rental.getEndDate());

        if(product != null){
            rental.setProduct(product);
            //TODO: something with old selected rental options
        }
    }

    private Rental runEditRentalMenu(Rental rental) throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);

        int option;
        do{
            System.out.println();
            System.out.println(rental);
            System.out.println();
            System.out.println("1. Change product.");
            System.out.println("2. Change the rental period.");
            System.out.println("3. Change pick-up address.");
            System.out.println("4. Change drop-off address.");
            System.out.println("5. Manage rental extra options.");
            System.out.println("6. Manage payment method.");
            System.out.println("7. Save and exit.");
            System.out.print("Choose an option: ");

            option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1 -> {
                    changeProduct(rental);
                }
                case 2 -> {
                    changeRentalPeriod(rental);
                }
                case 3 -> {
                    rental.setPickUpAddress(selectPickUpAddress());
                }
                case 4 -> {
                    rental.setReturnAddress(selectReturnAddress());
                }
                case 5 -> {
                    manageRentalOptions(rental, rental.getProduct());
                }
                case 6 -> {
                    managePayment(rental.getUser());
                }
                case 7 -> {
                    return rental;
                }
                default -> {
                    System.out.println("Please choose a valid option!");
                }
            }
        }
        while(true);

    }

}
