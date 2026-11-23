package ShoppingList;

import Recipe.Recipe;
import pantry.Pantry;

import java.util.ArrayList;

public abstract class ShoppingListGenerator {
    //VARIABLES
    protected ArrayList<IngredientStatus> ingredientToAddList = new ArrayList<>();

    //ABSTRACT METHODS
    public abstract void generateShoppingList(Recipe recipe, Pantry pantry); //main method to generate the shopping list, other methods are called from here

    //METHODS
    //check the availability of ingredients in the pantry
    public void checkAvailability(Pantry pantry, Recipe recipe) {
        //TODO: implement the logic to check the availability of ingredients in the pantry
        //TODO: add the ingredients to the ingredientToAddList
    }

    public void addIngredientsToList(){
        //TODO: implement the logic to add the ingredients to the shopping list
    }

}
