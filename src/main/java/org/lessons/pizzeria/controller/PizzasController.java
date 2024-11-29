package org.lessons.pizzeria.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.pizzeria.model.Pizza;
import org.lessons.pizzeria.model.SpecialOffer;
import org.lessons.pizzeria.repository.IngredientRepository;
import org.lessons.pizzeria.repository.PizzaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizzas")
public class PizzasController {

  @Autowired
  private PizzaRepository pizzaRepo;

  @Autowired
  private IngredientRepository ingredientRepo;

  @GetMapping
  public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {

    List<Pizza> allPizzas;

    if (keyword != null && !keyword.isBlank()) {
      allPizzas = pizzaRepo.findByNameContaining(keyword);
      model.addAttribute("keyword", keyword);
    } else {
      allPizzas = pizzaRepo.findAll();
    }

    model.addAttribute("pizzas", allPizzas);

    return "pizzas/index";
  }

  @GetMapping("/show/{id}")
  public String show(@PathVariable(name = "id") Long id,
      @RequestParam(name = "keyword", required = false) String keyword, Model model) {

    Optional<Pizza> pizzaById = pizzaRepo.findById(id);

    if (pizzaById.isPresent()) {
      model.addAttribute("pizza", pizzaById.get());
    }
    model.addAttribute("keyword", keyword);
    if (keyword == null || keyword.isBlank() || keyword.equals("null")) {
      model.addAttribute("pizzaUrl", "/pizzas");
    } else {
      model.addAttribute("pizzaUrl", "/?keyword=" + keyword);
    }

    return "pizzas/show";
  }

  @GetMapping("/create")
  public String create(Model model) {

    model.addAttribute("pizza", new Pizza());
    model.addAttribute("allIngredients", ingredientRepo.findAll());

    return "pizzas/create";
  }

  @PostMapping("/store")
  public String store(@Valid @ModelAttribute("pizza") Pizza formPizza,
      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "pizzas/create";
    }

    pizzaRepo.save(formPizza);

    redirectAttributes.addFlashAttribute("successMsg", "Pizza create");

    return "redirect:/pizzas";

  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {

    Optional<Pizza> pizzaById = pizzaRepo.findById(id);

    if (pizzaById.isPresent()) {
      model.addAttribute("pizza", pizzaById.get());
      model.addAttribute("allIngredients", ingredientRepo.findAll());
    }

    return "pizzas/edit";
  }

  @PostMapping("/edit/{id}")
  public String update(@Valid @ModelAttribute("pizza") Pizza formEditPizza,
      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("allIngredients", ingredientRepo.findAll());
      return "pizzas/edit";
    }

    pizzaRepo.save(formEditPizza);

    redirectAttributes.addFlashAttribute("updateMsg", "Pizza update");

    return "redirect:/pizzas";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

    pizzaRepo.deleteById(id);
    redirectAttributes.addFlashAttribute("deleteMsg", "Pizza deleted");

    return "redirect:/pizzas";
  }

  @GetMapping("/{id}/specialOffers")
  public String create(@PathVariable Long id, Model model) {

    Pizza pizzaById = pizzaRepo.findById(id).get();

    SpecialOffer specialOffers = new SpecialOffer();
    specialOffers.setPizza(pizzaById);

    model.addAttribute("specialOffers", specialOffers);

    return "/specialOffers/create";
  }

}