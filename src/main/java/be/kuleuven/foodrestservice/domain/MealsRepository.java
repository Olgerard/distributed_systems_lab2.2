package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();
    private static final Map<String, Order> orders = new HashMap<>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Optional<Meal> getCheapestMeal(){
        var values = meals.values();
        return values.stream().min(Comparator.comparing(Meal::getPrice));
    }

    public Optional<Meal> getLargestMeal(){
        var values = meals.values();
        return values.stream().max(Comparator.comparing(Meal::getKcal));
    }

    public Meal newMeal(Meal meal){
        Assert.notNull(meal, "The meal id must not be null");
        meals.put(meal.getId(), meal);
        return meal;
    }

    public Meal updateMeal(String id, Meal meal){
        Meal oldMeal = meals.get(id);
        oldMeal.setDescription(meal.getDescription());
        oldMeal.setKcal(meal.getKcal());
        oldMeal.setMealType(meal.getMealType());
        oldMeal.setName(meal.getName());
        oldMeal.setPrice(meal.getPrice());
        return oldMeal;
    }

    public Meal deleteMeal(String id){
        return meals.remove(id);
    }

    public OrderConfirmation addOrder(Order order){
        int price = 0;
        for (String mealId : order.getMeals()){
            Meal meal = meals.get(mealId);
            price += meal.getPrice();
        }
        OrderConfirmation  orderConfirmation = new OrderConfirmation();
        orderConfirmation.setPrice(price);
        orderConfirmation.setAddress(order.getAddress());
        orders.put(order.getAddress(), order);
        return orderConfirmation;
    }

    public Order getOrder(String address){
        return orders.get(address);
    }
}
