package model.product;


import java.util.Objects;

public class Bicycle extends Product{
    public enum Category {MOUNTAIN_BIKE, HYBRID_BIKE, CITY_BIKE, ROAD_BIKE, KIDS_BIKE, ELECTRIC_BIKE, BMX}

    private Category category;
    private int wheelSize;
    private String brakeSystem;


    public Bicycle(String name, float price, String manufacturer, String model, Category category, int wheelSize, String brakeSystem) {
        super(name, price, manufacturer, model);
        this.category = category;
        this.wheelSize = wheelSize;
        this.brakeSystem = brakeSystem;
    }


    public Bicycle(Product p, Category category, int wheelSize, String brakeSystem) {
        super(p);
        this.category = category;
        this.wheelSize = wheelSize;
        this.brakeSystem = brakeSystem;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    public String getBrakeSystem() {
        return brakeSystem;
    }

    public void setBrakeSystem(String brakeSystem) {
        this.brakeSystem = brakeSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bicycle bicycle = (Bicycle) o;
        return wheelSize == bicycle.wheelSize && category == bicycle.category && Objects.equals(brakeSystem, bicycle.brakeSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category, wheelSize, brakeSystem);
    }

    @Override
    public String toString() {
        return "Bicycle{" + super.toString() +
                "category=" + category +
                ", wheelSize=" + wheelSize +
                ", brakeSystem='" + brakeSystem + '\'' +
                "}";
    }
}
