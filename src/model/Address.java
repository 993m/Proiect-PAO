package model;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Scanner;

public class Address {
    private int id;
    private String city;
    private String streetName;
    private int streetNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address(String city, String streetName, int streetNumber) {
        this.city = city;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public Address(@NotNull Address adr) {
        this.id = adr.id;
        this.city = adr.city;
        this.streetName = adr.streetName;
        this.streetNumber = adr.streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    static public @NotNull Address readNewAddress(){
        Scanner sc = new Scanner(System.in);

        String city, streetName;
        int streetNumber;

        System.out.println("Enter the city: ");
        city = sc.nextLine();
        System.out.println("Enter the street name: ");
        streetName = sc.nextLine();
        System.out.println("Enter the street number: ");
        streetNumber = sc.nextInt();
        sc.nextLine();

        return new Address(city, streetName, streetNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return streetNumber == address.streetNumber && Objects.equals(city, address.city) && Objects.equals(streetName, address.streetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, streetName, streetNumber);
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNumber=" + streetNumber +
                '}';
    }
}
