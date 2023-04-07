package model.product;

import model.Rental;
import model.option.RentalOption;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Product {
    String name, manufacturer, model;
    float price;
    RentalOption[] options;

    Rental[] rentals;


    public Product(String name, float price, String manufacturer, String model, RentalOption options[]) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;

        this.options = new RentalOption[options.length];
        System.arraycopy(options, 0, this.options, 0, options.length);
    }

    public Product(Product p) {
        this.name = p.name;
        this.manufacturer = p.manufacturer;
        this.model = p.model;
        this.price = p.price;
        this.options = new RentalOption[p.options.length];
        System.arraycopy(p.options, 0, this.options, 0, p.options.length);
        System.arraycopy(p.rentals, 0, this.rentals, 0, p.options.length);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public RentalOption[] getOptions() {
        return options;
    }

    public void setOptions(RentalOption[] options) {
        this.options = options;
    }

    public Rental[] getRentals() {
        return rentals;
    }

    public void setRentals(Rental[] rentals) {
        this.rentals = rentals;
    }

    public void addOption(RentalOption option){
        int n=0;
        if(options!=null) n = options.length;

        RentalOption[] newOptions = new RentalOption[n + 1];

        System.arraycopy(this.options, 0, newOptions, 0, n);

        newOptions[n + 1] = option;

        this.options = new RentalOption[n + 1];
        System.arraycopy(newOptions, 0, this.options, 0, n + 1);

    }

    public void addRental(Rental r){
        int n=0;
        if(rentals!=null) n = rentals.length;

        Rental[] newRentals = new Rental[n + 1];

        System.arraycopy(this.rentals, 0, newRentals, 0, n);

        newRentals[n + 1] = r;

        this.rentals = new Rental[n + 1];
        System.arraycopy(newRentals, 0, this.rentals, 0, n + 1);

        //sortare
        Arrays.sort(rentals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Float.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(manufacturer, product.manufacturer) && Objects.equals(model, product.model) && Arrays.equals(options, product.options) && Arrays.equals(rentals, product.rentals);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, manufacturer, model, price);
        result = 31 * result + Arrays.hashCode(options);
        result = 31 * result + Arrays.hashCode(rentals);
        return result;
    }

    @Override
    public String toString() {
        return "Product: " + name + ' ' + manufacturer + ' ' + model + " - " + price + " euros/day\n";
    }

    public boolean isRentable(Date startDate, Date endDate){
        if(rentals == null) return true;

        int index;
        Date rentalStartDate=null, rentalEndDate;
        for(index = 0; index < rentals.length; index++){
            rentalStartDate = rentals[index].getStartDate();
            rentalEndDate = rentals[index].getEndDate();
            if(startDate.compareTo(rentalStartDate)>0) break;
            if(endDate.compareTo(rentalEndDate)>0) return false;
        }

        if(rentalStartDate!=null && endDate.compareTo(rentalStartDate)>0) return true;

        return false;
    }

    public void showOptions(){
        for(int i=0; i< options.length; i++)
            System.out.println(i+1 + options[i].toString());
    }
}
