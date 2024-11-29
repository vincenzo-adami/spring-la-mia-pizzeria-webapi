package org.lessons.pizzeria.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
public class SpecialOffer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @FutureOrPresent(message = "Starting date cannot start before today")
  @NotNull(message = "Starting date cannot be null")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startingDate;

  @FutureOrPresent(message = "Ending date cannot end before start")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endingDate;

  @NotNull(message = "Title cannot be null")
  private String title;

  @ManyToOne
  @JoinColumn(name = "pizza_id", nullable = false)
  @JsonBackReference
  private Pizza pizza;

  public Long getId() {
    return id;
  }

  public Pizza getPizza() {
    return pizza;
  }

  public void setPizza(Pizza pizza) {
    this.pizza = pizza;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getStartingDate() {
    return startingDate;
  }

  public void setStartingDate(LocalDate startingDate) {
    this.startingDate = startingDate;
  }

  public LocalDate getEndingDate() {
    return endingDate;
  }

  public void setEndingDate(LocalDate endingDate) {
    this.endingDate = endingDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "SpecialOffers [id=" + id + ", startingDate=" + startingDate + ", endingDate=" + endingDate + ", title="
        + title + "]";
  }

}