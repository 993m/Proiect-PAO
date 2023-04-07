package model.option;

public class RentalOption {
    String description;
    float price;


    public RentalOption(String description, float price) {
        this.description = description;
        this.price = price;
    }

    public RentalOption(RentalOption opt) {
        this.description = opt.description;
        this.price = opt.price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Rental option: " + description + " - " + price + " euros\n";
    }

}
