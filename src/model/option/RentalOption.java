package model.option;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class RentalOption {
    private int id;
    protected String description;
    protected float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RentalOption(String description, float price) {
        this.description = description;
        this.price = price;
    }

    public RentalOption(@NotNull RentalOption opt) {
        this.id = opt.id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalOption that = (RentalOption) o;
        return Float.compare(that.price, price) == 0 && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, price);
    }

    @Override
    public String toString() {
        return "RentalOption{" +
                "description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
