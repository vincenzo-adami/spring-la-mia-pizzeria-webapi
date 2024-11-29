package org.lessons.pizzeria.api.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.pizzeria.api.model.Payload;
import org.lessons.pizzeria.model.Pizza;
import org.lessons.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/api/pizzas")
public class PizzasRestController {

  @Autowired
  private PizzaRepository pizzaRepo;

  @GetMapping()
  public ResponseEntity<Payload<List<Pizza>>> index(@RequestParam(required = false) String keyword) {

    try {

      if (keyword != null && !keyword.isBlank()) {

        Payload<List<Pizza>> respone = new Payload<>("Ok", "200", pizzaRepo.findByNameContaining(keyword));

        return new ResponseEntity<Payload<List<Pizza>>>(respone, HttpStatus.OK);

      } else {

        Payload<List<Pizza>> respone = new Payload<>("OK", "200", pizzaRepo.findAll());
        return ResponseEntity.ok(respone);

      }

    } catch (Exception e) {

      Payload<List<Pizza>> error = new Payload<List<Pizza>>("Search for keyword has an error: " + e.getMessage(), "400",
          null);

      return new ResponseEntity<Payload<List<Pizza>>>(error, HttpStatus.BAD_REQUEST);

    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Payload<Pizza>> show(@PathVariable Long id) {

    try {

      Optional<Pizza> byId = pizzaRepo.findById(id);

      Payload<Pizza> response = new Payload<Pizza>("Ok", "200", byId.get());

      return ResponseEntity.ok(response);

    } catch (Exception e) {

      Payload<Pizza> error = new Payload<Pizza>("Pizza not found", "404", null);

      return new ResponseEntity<Payload<Pizza>>(error, HttpStatus.NOT_FOUND);

    }

  }

  @PostMapping()
  public ResponseEntity<Payload<Pizza>> create(@Valid @RequestBody Pizza pizza) {
    pizzaRepo.save(pizza);
    Payload<Pizza> respone = new Payload<Pizza>("Created", "201", pizza);

    return ResponseEntity.ok(respone);

  }

  @PutMapping("/{id}")
  public ResponseEntity<Payload<Pizza>> update(@PathVariable Long id, @RequestBody Pizza pizza) {

    try {

      Optional<Pizza> byId = pizzaRepo.findById(id);

      Pizza dbPizza = byId.get();

      dbPizza.setName(pizza.getName());
      dbPizza.setIngredients(pizza.getIngredients());
      dbPizza.setPhotoUrl(pizza.getPhotoUrl());
      dbPizza.setPrice(pizza.getPrice());
      dbPizza.setDescription(pizza.getDescription());

      pizzaRepo.save(dbPizza);

      Payload<Pizza> response = new Payload<Pizza>("Ok", "200", dbPizza);

      return ResponseEntity.ok(response);

    } catch (Exception e) {

      Payload<Pizza> response = new Payload<Pizza>("Pizza not found", "404", null);

      return new ResponseEntity<Payload<Pizza>>(response, HttpStatus.NOT_FOUND);

    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Payload<Pizza>> delete(@PathVariable Long id) {

    try {

      Payload<Pizza> response = new Payload<Pizza>("Deleted", "200", pizzaRepo.findById(id).get());
      pizzaRepo.deleteById(id);

      return ResponseEntity.ok(response);

    } catch (Exception e) {

      Payload<Pizza> response = new Payload<Pizza>("Pizza not found", "404", null);

      return new ResponseEntity<Payload<Pizza>>(response, HttpStatus.NOT_FOUND);

    }
  }

}
