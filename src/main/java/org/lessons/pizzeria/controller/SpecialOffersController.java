package org.lessons.pizzeria.controller;

import java.util.Optional;

import org.lessons.pizzeria.model.SpecialOffer;
import org.lessons.pizzeria.repository.PizzaRepository;
import org.lessons.pizzeria.repository.SpecialOfferRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/specialOffers")
public class SpecialOffersController {

  @Autowired
  SpecialOfferRepository specialOfferRepository;

  @Autowired
  PizzaRepository pizzaRepository;

  @PostMapping("/store")
  public String store(@Valid @ModelAttribute(name = "specialOffers") SpecialOffer specialOffersForm,
      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    Long pizzaID = specialOffersForm.getPizza().getId();

    if (bindingResult.hasErrors()) {
      return "specialOffers/create";
    }

    specialOfferRepository.save(specialOffersForm);

    redirectAttributes.addFlashAttribute("successMsg", "Special Offers created");

    return "redirect:/pizzas/show/" + pizzaID + "";

  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    Optional<SpecialOffer> specialOffersById = specialOfferRepository.findById(id);

    if (specialOffersById.isPresent()) {
      model.addAttribute("specialOffers", specialOffersById.get());
    }

    return "/specialOffers/edit";
  }

  @PostMapping("/edit/{id}")
  public String update(@Valid @ModelAttribute("specialOffers") SpecialOffer formSpecialOffers,
      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "/specialOffers/edit";
    }

    specialOfferRepository.save(formSpecialOffers);

    redirectAttributes.addFlashAttribute("updateMsg", "Special Offers updated");

    Long pizzaID = formSpecialOffers.getPizza().getId();

    return "redirect:/pizzas/show/" + pizzaID + "";
  }

}
