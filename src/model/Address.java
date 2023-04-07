package model;

public class Address {
    String city;
    String streetName;
    int streetNumber;


    public Address(String city, String streetName, int streetNumber) {
        this.city = city;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public Address(Address adr) {
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
}
