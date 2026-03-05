package be.kuleuven.foodrestservice.domain;

public class OrderConfirmation {
    protected String address;
    protected int price;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
