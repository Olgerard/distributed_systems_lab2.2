package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderConfirmation;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @PostMapping("/restrpc/meals")
    ResponseEntity<Void> newMeal(@RequestBody Meal meal) {
        Meal createdmeal = mealsRepository.newMeal(meal);
        if (createdmeal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/restrpc/meals/{id}")
    ResponseEntity<Void> updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        Meal updatedmeal = mealsRepository.updateMeal(id, meal);
        if(updatedmeal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/restrpc/meals/{id}")
    ResponseEntity<Void> deleteMeal(@PathVariable String id) {
        Meal updatedmeal = mealsRepository.deleteMeal(id);
        if(updatedmeal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/restrpc/orders")
    OrderConfirmation addOrder(@RequestBody Order order) {
        return mealsRepository.addOrder(order);
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/cheapestmeal")
    Optional<Meal> getCheapestMeal() {
        return mealsRepository.getCheapestMeal();
    }

    @GetMapping("restrpc/largestmeal")
    Optional<Meal> getLargestMeal(){return mealsRepository.getLargestMeal();}
}
