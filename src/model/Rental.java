package model;

import model.option.AddressRentalOption;
import model.option.RentalOption;
import model.product.Product;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static model.Address.readNewAddress;

public class Rental implements Comparable<Rental>{
    private Product product;
    private User user;
    private Date startDate, endDate;
    private float totalPrice;
    private ArrayList<RentalOption> rentalOptions = new ArrayList<RentalOption>();
    private Address pickUpAddress, returnAddress;


    public Rental(Product product, User user, Date startDate, Date endDate, Address pickUpAddress, Address returnAddress) {
        this.product = product;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickUpAddress = pickUpAddress;
        this.returnAddress = returnAddress;

        totalPrice = calculateTotal();

        this.user.addRentalToHistory(this);
        this.product.addRental(this);
    }

    public Rental(@NotNull Rental r){
        this.product = r.product;
        this.user = r.user;
        this.startDate = r.startDate;
        this.endDate = r.endDate;
        this.pickUpAddress = r.pickUpAddress;
        this.returnAddress = r.returnAddress;
        this.totalPrice = r.totalPrice;

        rentalOptions = new ArrayList<RentalOption>();
        for(RentalOption option: r.getRentalOptions()){
            this.rentalOptions.add(option);
        }
    }


    // for sorting rentals by startDate in descending order
    @Override
    public int compareTo(@NotNull Rental r){
        return -this.startDate.compareTo(r.startDate);
    }

    private float calculateTotal(){
        float s = this.product.getPrice();
        for(RentalOption o: this.rentalOptions)
            s += o.getPrice();

        return s;
    }

    private void updateTotalPrice(){
        this.totalPrice = calculateTotal();
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

    public ArrayList<RentalOption> getRentalOptions() {
        return rentalOptions;
    }

    public void setRentalOptions(ArrayList<RentalOption> rentalOptions) {
        this.rentalOptions = rentalOptions;
    }

    public void addRentalOption(RentalOption r){
        // check that option doesn't already exist
        for(RentalOption opt: rentalOptions)
            if(opt.equals(r)) {
                System.out.println("The option has already been added.");
                return;
            }

        if(r instanceof AddressRentalOption){
            System.out.println("You need to enter an address for this option: ");
            Address address = readNewAddress();
            ((AddressRentalOption) r).setAddress(address);
        }

        rentalOptions.add(r);
        System.out.println("The option has been added.");
    }

    public void deleteRentalOption(RentalOption r){
        rentalOptions.remove(r);
        System.out.println("The option has been removed.");
    }

    public void showRentalOptions(){
        if(rentalOptions.size() == 0) {
            System.out.println("There are no rental options chosen.");
        }
        else{
            for(int i=0; i<rentalOptions.size(); i++){
                System.out.println((i+1) + ". " + rentalOptions.get(i));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Float.compare(rental.totalPrice, totalPrice) == 0 && Objects.equals(product, rental.product) && Objects.equals(user, rental.user) && Objects.equals(startDate, rental.startDate) && Objects.equals(endDate, rental.endDate) && Objects.equals(rentalOptions, rental.rentalOptions) && Objects.equals(pickUpAddress, rental.pickUpAddress) && Objects.equals(returnAddress, rental.returnAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, user, startDate, endDate, totalPrice, rentalOptions, pickUpAddress, returnAddress);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "product=" + product +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPrice=" + totalPrice +
                ", rentalOptions=" + rentalOptions +
                ", pickUpAddress=" + pickUpAddress +
                ", returnAddress=" + returnAddress +
                '}';
    }
}
