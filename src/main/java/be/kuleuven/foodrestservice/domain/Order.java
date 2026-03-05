package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class Order {
    protected String address;
    protected List<String> meals;

    public List<String> getMeals() {
        return meals;
    }
    public void setMeals(List<String> meals) {
        this.meals = meals;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
