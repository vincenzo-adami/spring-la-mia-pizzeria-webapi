package org.lessons.pizzeria.repository;

import java.util.List;
import java.util.Optional;

import org.lessons.pizzeria.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

  List<Ingredient> findByNameContaining(String name);

  Optional<Ingredient> findByName(String name);
}
