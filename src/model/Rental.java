package model;

import databaseAccess.AddressCRUD;
import databaseAccess.AddressRentalOptionCRUD;
import databaseAccess.RentalOptionCRUD;
import model.option.AddressRentalOption;
import model.option.RentalOption;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static model.Address.readNewAddress;

public class Rental implements Comparable<Rental>{
    private int id;
    private Product product;
    private User user;
    private Date startDate, endDate;
    private float totalPrice;
    private Address pickUpAddress, returnAddress;

    private static final RentalOptionCRUD rentalOptionsCRUD = RentalOptionCRUD.getInstance();
    private static final AddressRentalOptionCRUD addressRentalOptionsCRUD = AddressRentalOptionCRUD.getInstance();
    private static final AddressCRUD addressCRUD = AddressCRUD.getInstance();


    public Rental(Product product, User user, Date startDate, Date endDate, Address pickUpAddress, Address returnAddress) throws SQLException {
        this.product = product;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickUpAddress = pickUpAddress;
        this.returnAddress = returnAddress;

        totalPrice = calculateTotal();
    }

    public Rental(@NotNull Rental r) {
        this.id = r.id;
        this.product = r.product;
        this.user = r.user;
        this.startDate = r.startDate;
        this.endDate = r.endDate;
        this.totalPrice = r.totalPrice;
        this.pickUpAddress = r.pickUpAddress;
        this.returnAddress = r.returnAddress;
    }

    // for sorting rentals by startDate in descending order
    @Override
    public int compareTo(@NotNull Rental r){
        return -this.startDate.compareTo(r.startDate);
    }

    private float calculateTotal() throws SQLException {
        float s = this.product.getPrice();
        for(RentalOption o: rentalOptionsCRUD.getAllForRental(this))
            s += o.getPrice();

        return s;
    }

    private void updateTotalPrice() throws SQLException {
        this.totalPrice = calculateTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Address getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(Address pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public Address getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(Address returnAddress) {
        this.returnAddress = returnAddress;
    }





    public void showRentalOptions() throws SQLException {
        ArrayList<RentalOption> options = rentalOptionsCRUD.getAllForRental(this);

        if(options.size() == 0) {
            System.out.println("There are no rental options chosen.");
        }
        else{
            for(int i=0; i<options.size(); i++){
                System.out.println((i+1) + ". " + options.get(i));
            }
        }
    }

    public ArrayList<RentalOption> getOptions() throws SQLException {
        return rentalOptionsCRUD.getAllForRental(this);
    }
    public void addRentalOption(RentalOption r) throws SQLException {
        // check that option doesn't already exist
        for(RentalOption opt: rentalOptionsCRUD.getAllForRental(this))
            if(opt.getId() == r.getId()) {
                System.out.println("The option has already been added.");
                return;
            }

        if(rentalOptionsCRUD.checkIfAddressOption(r)){
            System.out.println("You need to enter an address for this option: ");
            Address address = readNewAddress();
            addressCRUD.add(address);
            AddressRentalOption newOption = new AddressRentalOption(r, address);
            addressRentalOptionsCRUD.add(newOption);
            addressRentalOptionsCRUD.addForRental(newOption, this);
        }
        else {
            rentalOptionsCRUD.addForRental(r, this);
        }

        System.out.println("The option has been added.");
    }

    public void deleteRentalOption(RentalOption r) throws SQLException{
        if(r instanceof AddressRentalOption){
            addressRentalOptionsCRUD.delete(r.getId());
        }

        rentalOptionsCRUD.deleteForRental(r, this);
        System.out.println("The option has been removed.");
    }

    public ArrayList<AddressRentalOption> getAddressOptions() throws SQLException {
        return addressRentalOptionsCRUD.getAllForRental(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Float.compare(rental.totalPrice, totalPrice) == 0 && Objects.equals(product, rental.product) && Objects.equals(user, rental.user) && Objects.equals(startDate, rental.startDate) && Objects.equals(endDate, rental.endDate) && Objects.equals(pickUpAddress, rental.pickUpAddress) && Objects.equals(returnAddress, rental.returnAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, user, startDate, endDate, totalPrice, pickUpAddress, returnAddress);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "product=" + product +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPrice=" + totalPrice +
                ", pickUpAddress=" + pickUpAddress +
                ", returnAddress=" + returnAddress +
                '}';
    }
}
