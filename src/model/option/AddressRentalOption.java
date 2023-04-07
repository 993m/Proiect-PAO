package model.option;

import model.Address;

public class AddressRentalOption extends RentalOption{
    Address address;


    public AddressRentalOption(String description, float price, Address address) {
        super(description, price);
        this.address = address;
    }

    public AddressRentalOption(AddressRentalOption opt) {
        super(opt);
        this.address = opt.address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return super.toString() + this.address.toString();
    }
}
