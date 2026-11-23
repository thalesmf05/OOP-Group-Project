/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recipe;

import java.util.ArrayList;

/**
 *
 * @author thale
 */
public class Recipe {
    private static int nextId = 1; //static variable to keep track of the next id
    private static int countRecipes = 0; //count the number of recipes

    private final int id;//unique id for each recipe
    private String name;
    private Double timeInMinutes;
    private String mealType;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;

    ArrayList<Ingredient> ingredients = new ArrayList<>(); //arraylist of ingredients


    //CONSTRUCTORS
    //new recipe, automatically generates a new id
    public Recipe(String name, double carbs, double fat, double protein, double calories, String mealType, Double timeInMinutes, ArrayList<Ingredient> ingredients) {
        this.id = nextId; //set the id to the nextId
        this.name = name;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.calories = calories;
        this.mealType = mealType;
        this.timeInMinutes = timeInMinutes;
        this.ingredients = ingredients;
        nextId++; //increment the nextId for the next recipe
        countRecipes++; //increment the count of recipes
    }

    //recipe that already exists and will be edited, this doesn't generate a new id rather sets the old recipe id
    public Recipe(int id, String name, Double timeInMinutes, String mealType, double calories, double protein, double fat, double carbs, ArrayList<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.timeInMinutes = timeInMinutes;
        this.mealType = mealType;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.ingredients = ingredients;
    }

    //GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getTimeInMinutes() {
        return timeInMinutes;
    }

    public String getMealType() {
        return mealType;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    } //returns a copy of the ingredients arraylist


    //INGREDIENT MANAGEMENT METHODS
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
    }

    //there is no need to get the id from the old ingredient object as the new object was created using the same id on the ingredient constructor
    public void editIngredient(Ingredient updatedIngredient){
        ingredients.set(updatedIngredient.getId(), updatedIngredient); //replace the old ingredient with the updated ingredient
    }

    //OTHER METHODS
    public int countRecipes(){ //returns the number of recipes
        return ingredients.size();
    }







}
