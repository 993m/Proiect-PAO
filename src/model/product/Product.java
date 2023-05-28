package model.product;

import databaseAccess.RentalCRUD;
import databaseAccess.RentalOptionCRUD;
import model.Rental;
import model.option.RentalOption;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class Product implements Comparable<Product>{
    private int id;
    protected String name;
    protected String manufacturer;
    protected String model;
    protected float price;


    private static final RentalCRUD rentalCRUD = RentalCRUD.getInstance();
    private static final RentalOptionCRUD rentalOptionCRUD = RentalOptionCRUD.getInstance();

    public Product(@NotNull Product p) {
        this.id = p.id;
        this.name = p.name;
        this.manufacturer = p.manufacturer;
        this.model = p.model;
        this.price = p.price;
    }

    @Override
    public int compareTo(@NotNull Product p){
        if(this.price < p.getPrice()){
            return -1;
        }
        return 1;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product(String name, float price, String manufacturer, String model) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Float.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(manufacturer, product.manufacturer) && Objects.equals(model, product.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, model, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                '}';
    }

    public boolean isRentable(Date startDate, Date endDate) throws SQLException {
        ArrayList<Rental> rentals = rentalCRUD.getRentalsForProduct(this);
        Collections.sort(rentals);

        if (rentals.size() == 0) return true;

        int index;
        Date rentalStartDate = null, rentalEndDate;
        for (Rental r: rentals) {
            rentalStartDate = r.getStartDate();
            rentalEndDate = r.getEndDate();
            if (startDate.compareTo(rentalStartDate) > 0) break;
            if (endDate.compareTo(rentalEndDate) > 0) return false;
        }

        if (rentalStartDate != null && endDate.compareTo(rentalStartDate) > 0) return true;

        return false;
    }



    public void showOptions() throws SQLException {
        ArrayList<RentalOption> options = rentalOptionCRUD.getAllForProduct(this);

        if (options.size() == 0) {
            System.out.println("There are no rental options available.");
        } else {
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
        }
    }

    public ArrayList<RentalOption> getOptions() throws SQLException {
        return rentalOptionCRUD.getAllForProduct(this);
    }
}
