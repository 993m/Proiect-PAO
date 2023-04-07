package model;

import model.option.RentalOption;
import model.product.Product;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Rental implements Comparable<Rental>{
    Product product;
    User user;
    Date startDate, endDate;
    float totalPrice;
    RentalOption[] rentalOptions;
    Address pickUpAddress=null, returnAddress=null;


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

    // for sorting rentals by startDate in descending order
    public int compareTo(Rental r){
        return -this.startDate.compareTo(r.startDate);
    }

    public float calculateTotal(){
        float s = this.product.getPrice();
        for(RentalOption o: this.rentalOptions)
            s += o.getPrice();

        return s;
    }

    public void updateTotalPrice(){
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

    public void addRentalOption(RentalOption r){
        // VERIFICA DACA NU AI INTRODUS DEJA ACEA OPTIUNE



        int n=0;
        if(rentalOptions!=null) n = rentalOptions.length;

        RentalOption[] newRentalOptions = new RentalOption[n + 1];

        System.arraycopy(this.rentalOptions, 0, newRentalOptions, 0, n);

        newRentalOptions[n + 1] = r;

        this.rentalOptions = new RentalOption[n + 1];
        System.arraycopy(newRentalOptions, 0, this.rentalOptions, 0, n + 1);
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
        return "Rental: " +
                "product=" + product +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPrice=" + totalPrice + "\n" +
                "pickUpAddress: " + pickUpAddress.toString() +
                "returnAddress=" + returnAddress.toString() + "\n";
    }
}
