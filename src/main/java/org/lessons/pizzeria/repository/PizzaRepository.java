package org.lessons.pizzeria.repository;

import org.lessons.pizzeria.model.Pizza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

  List<Pizza> findByNameContaining(String name);
}
