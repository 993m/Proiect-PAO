package model.product;

import model.option.RentalOption;

import java.util.Objects;

public class Car extends Product{
    enum Transmission {AUTOMATIC, MANUAL}
    enum EngineType {GAS, DIESEL, HYBRID}
    enum Drivetrain {RWD, FWD, AWD}
    enum Class {COMPACT, FULL_SIZE, SUV, VAN, ECONOMY, LUXURY}


    int seats;
    Transmission transmission;
    int horsepower;
    EngineType engineType;
    Drivetrain drivetrain;
    Class carClass;
    int kmLimit;
    int speedLimit;


    public Car(String name, float price, String manufacturer, String model, RentalOption[] options, int seats, Transmission transmission, int horsepower, EngineType engineType, Drivetrain drivetrain, Class carClass, int speedLimit, int kmLimit) {
        super(name, price, manufacturer, model, options);
        this.seats = seats;
        this.transmission = transmission;
        this.horsepower = horsepower;
        this.engineType = engineType;
        this.drivetrain = drivetrain;
        this.carClass = carClass;
        this.speedLimit = speedLimit;
        this.kmLimit = kmLimit;
    }

    public Car(Car car) {
        super(car);
        this.seats = car.seats;
        this.transmission = car.transmission;
        this.horsepower = car.horsepower;
        this.engineType = car.engineType;
        this.drivetrain = car.drivetrain;
        this.carClass = car.carClass;
        this.speedLimit = car.speedLimit;
        this.kmLimit = car.kmLimit;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public Class getCarClass() {
        return carClass;
    }

    public void setCarClass(Class carClass) {
        this.carClass = carClass;
    }

    public int getKmLimit() {
        return kmLimit;
    }

    public void setKmLimit(int kmLimit) {
        this.kmLimit = kmLimit;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Car car = (Car) o;
        return seats == car.seats && horsepower == car.horsepower && kmLimit == car.kmLimit && speedLimit == car.speedLimit && transmission == car.transmission && engineType == car.engineType && drivetrain == car.drivetrain && carClass == car.carClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), seats, transmission, horsepower, engineType, drivetrain, carClass, kmLimit, speedLimit);
    }

    @Override
    public String toString() {
        return super.toString() + "Details: " +
                "Number of seats=" + seats +
                ", transmission=" + transmission +
                ", horsepower=" + horsepower +
                ", engineType=" + engineType +
                ", drivetrain=" + drivetrain +
                ", carClass=" + carClass +
                ", limit of kilometers=" + kmLimit +
                ", limit of speed=" + speedLimit + "\n";
    }
}
