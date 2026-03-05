package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderConfirmation;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/cheapestmeal")
    EntityModel<Meal> getCheapestMeal() {
        Meal meal = mealsRepository.getCheapestMeal().orElseThrow(() -> new MealNotFoundException("cheapest meal"));
        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/largestmeal")
    EntityModel<Meal> getLargestMeal() {
        Meal meal = mealsRepository.getLargestMeal().orElseThrow(() -> new MealNotFoundException("largest meal"));
        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @PostMapping("/rest/meals")
    ResponseEntity<Void> newMeal(@RequestBody Meal meal) {
        Meal createdmeal = mealsRepository.newMeal(meal);
        if (createdmeal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/rest/meals/{id}")
    ResponseEntity<Void> updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        Meal updatedmeal = mealsRepository.updateMeal(id, meal);
        if (updatedmeal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/rest/meals/{id}")
    ResponseEntity<Void> deleteMeal(@PathVariable String id) {
        Meal updatedmeal = mealsRepository.deleteMeal(id);
        if (updatedmeal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/rest/orders")
    ResponseEntity<EntityModel<OrderConfirmation>> addOrder(@RequestBody Order order) {
        OrderConfirmation confirmation = mealsRepository.addOrder(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EntityModel.of(confirmation,
                        linkTo(methodOn(MealsRestController.class).addOrder(null)).withSelfRel()));
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"),
                linkTo(methodOn(MealsRestController.class).addOrder(null)).withRel("rest/orders"));
    }
}