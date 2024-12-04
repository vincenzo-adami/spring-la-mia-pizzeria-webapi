package org.lessons.pizzeria.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.pizzeria.model.Ingredient;
import org.lessons.pizzeria.model.Pizza;
import org.lessons.pizzeria.repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngredientsController {

  @Autowired
  private IngredientRepository ingredientRepo;

  @GetMapping
  public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {

    List<Ingredient> allIngredients;

    if (keyword != null && !keyword.isBlank()) {
      allIngredients = ingredientRepo.findByNameContaining(keyword);
      model.addAttribute("keyword", keyword);
    } else {
      allIngredients = ingredientRepo.findAll();
    }

    model.addAttribute("ingredient", new Ingredient());
    model.addAttribute("ingredients", allIngredients);

    return "ingredients/index";
  }

  @PostMapping("/store")
  public String store(@Valid @ModelAttribute Ingredient ingredient, BindingResult bindingResult, Model model,
      @RequestParam(name = "keyword", required = false) String keyword, RedirectAttributes redirectAttributes) {

    if (ingredientRepo.findByName(ingredient.getName()).isPresent()) {
      // bindingResult.addError(new ObjectError("unique", "This ingredient already
      // exists"));
      bindingResult.addError(new FieldError("unique", "name", "This ingredient already exists"));
    }

    if (bindingResult.hasErrors()) {
      List<Ingredient> allIngredients;

      if (keyword != null && !keyword.isBlank()) {
        allIngredients = ingredientRepo.findByNameContaining(keyword);
        model.addAttribute("keyword", keyword);
      } else {
        allIngredients = ingredientRepo.findAll();
      }

      model.addAttribute("ingredient", ingredient);
      model.addAttribute("ingredients", allIngredients);

      return "ingredients/index";
    }
    ingredientRepo.save(ingredient);
    redirectAttributes.addFlashAttribute("successMsg", "Ingredient create");

    return "redirect:/ingredients";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    Optional<Ingredient> ingredientById = ingredientRepo.findById(id);

    if (ingredientById.isPresent()) {
      model.addAttribute("ingredient", ingredientById.get());
    }

    return "ingredients/edit";
  }

  @PostMapping("/edit/{id}")
  public String update(@Valid @ModelAttribute("ingredient") Ingredient formEditIngredient, BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "ingredients/edit";
    }

    ingredientRepo.save(formEditIngredient);
    redirectAttributes.addFlashAttribute("updateMsg", "Ingredient Update");

    return "redirect:/ingredients";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

    Ingredient ingredient = ingredientRepo.findById(id).get();

    for (Pizza pizza : ingredient.getPizzas()) {

      pizza.getIngredients().remove(ingredient);

    }

    ingredientRepo.deleteById(id);
    redirectAttributes.addFlashAttribute("deleteMsg", "Ingredient delete");

    return "redirect:/ingredients";
  }

}