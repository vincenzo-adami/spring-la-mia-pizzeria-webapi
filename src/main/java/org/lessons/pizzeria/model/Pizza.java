package org.lessons.pizzeria.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pizza")
public class Pizza {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @NotBlank(message = "Name cannot be empty")
  @Size(max = 40, message = "Name too long, max 40 character")
  private String name;

  @NotBlank(message = "Description cannot be empty")
  private String description;

  @NotNull
  @NotBlank(message = "Photo URL cannot empty")
  private String photoUrl;

  @NotNull(message = "Price cannot be null")
  @Positive(message = "Price must be greater than 0")
  private Double price;

  @OneToMany(mappedBy = "pizza")
  private List<SpecialOffer> specialOffers;

  @ManyToMany()
  @JoinTable(name = "pizza_ingredient", joinColumns = @JoinColumn(name = "pizza_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
  private List<Ingredient> ingredients;

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public Long getId() {
    return id;
  }

  public List<SpecialOffer> getSpecialOffers() {
    return specialOffers;
  }

  public void setSpecialOffers(List<SpecialOffer> specialOffers) {
    this.specialOffers = specialOffers;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "pizza [id=" + id + ", name=" + name + ", description=" + description + ", photoUrl=" + photoUrl + ", price="
        + price + "]";
  }

}