package model.product;

import model.Rental;
import model.option.RentalOption;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Product {
    protected String name, manufacturer, model;
    protected float price;
    protected ArrayList<RentalOption> options = new ArrayList<RentalOption>();
    protected ArrayList<Rental> rentals = new ArrayList<Rental>();

    public Product(String name, float price, String manufacturer, String model) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
    }

    public Product(String name, float price, String manufacturer, String model, ArrayList<RentalOption> options) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;

        this.options = new ArrayList<RentalOption>();
        for (RentalOption opt : options) {
            this.options.add(new RentalOption(opt));
        }
    }

    public Product(Product p) {
        this.name = p.name;
        this.manufacturer = p.manufacturer;
        this.model = p.model;
        this.price = p.price;

        this.rentals = new ArrayList<Rental>();
        for (Rental r : p.getRentals()) {
            this.rentals.add(new Rental(r));
        }

        this.options = new ArrayList<RentalOption>();
        for (RentalOption opt : p.getOptions()) {
            this.options.add(new RentalOption(opt));
        }
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

    public ArrayList<RentalOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<RentalOption> options) {
        this.options = options;
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(ArrayList<Rental> rentals) {
        this.rentals = rentals;
    }

    public void addOption(RentalOption option) {
        options.add(option);
    }

    public void addRental(Rental r) {
        rentals.add(r);
        Collections.sort(rentals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Float.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(manufacturer, product.manufacturer) && Objects.equals(model, product.model) && Objects.equals(options, product.options) && Objects.equals(rentals, product.rentals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, model, price, options, rentals);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", options=" + options +
                ", rentals=" + rentals +
                '}';
    }

    public boolean isRentable(Date startDate, Date endDate) {
        if (rentals.size() == 0) return true;

        int index;
        Date rentalStartDate = null, rentalEndDate;
        for (index = 0; index < rentals.size(); index++) {
            rentalStartDate = rentals.get(index).getStartDate();
            rentalEndDate = rentals.get(index).getEndDate();
            if (startDate.compareTo(rentalStartDate) > 0) break;
            if (endDate.compareTo(rentalEndDate) > 0) return false;
        }

        if (rentalStartDate != null && endDate.compareTo(rentalStartDate) > 0) return true;

        return false;
    }

    public void showOptions() {
        if (options.size() == 0) {
            System.out.println("There are no rental options available.");
        } else {
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
        }

    }
}
