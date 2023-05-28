package model.option;

import model.Address;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddressRentalOption extends RentalOption{
    private Address address;

    public AddressRentalOption(String description, float price) {
        super(description, price);
    }

    public AddressRentalOption(AddressRentalOption opt) {
        super(opt);
        this.address = opt.address;
    }

    public AddressRentalOption(@NotNull RentalOption opt, Address address) {
        super(opt);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddressRentalOption that = (AddressRentalOption) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address);
    }

    @Override
    public String toString() {
        return "AddressRentalOption{" +
                ", description='" + description + '\'' +
                "address=" + address +
                ", price=" + price +
                '}';
    }
}
