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
public class RecipeManager {
    ArrayList<Recipe> recipes = new ArrayList<Recipe>(); //list of recipes

    //CONSTRUCTOR


    public RecipeManager() {}

    //GETTERS AND SETTERS
    public ArrayList<Recipe> getRecipes(){ //returns a copy of the recipes arraylist
        return recipes;
    }

    public Recipe getRecipe(int id){
        return recipes.get(id);
    }

    //RECIPE MANAGEMENT METHODS
    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }

    //there is no need to get the id from the old recipe object as the new object was created using the same id on the recipe constructor
    public void editRecipe(Recipe updatedRecipe){ //replace the old recipe with the updated recipe
        recipes.set(updatedRecipe.getId(), updatedRecipe);
    }

    public void removeRecipe(Recipe recipe){
        recipes.remove(recipe);
    }

    //SEARCH METHODS
    public Recipe searchRecipeByName(String name){
        for(Recipe recipe : recipes){
            if(recipe.getName().equals(name)){
                return recipe;
            }
        }
        return null;
    }

    //TODO save and load from file
}
