/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dailydiary;

/**
 * Abstract superclass representing a 
 * food entry in the Daily Diary.
 * 
 * @author matheusreis
 */
public abstract class FoodEntry {
    //data members
     private String foodName;
     private String mealType;
     private String quantity;
     private double calories;
     private double protein;
     private double carbs;
     private double fat;
     
    //constructor
    public FoodEntry(String foodName, String mealType, String quantity, double calories, double protein, double carbs, double fat) {
        
        //Validate inputs
        validateText(foodName,"Food Name");
        validateText(mealType,"Meal type");
        validateText(quantity,"Qauntity");
        
        validateNutritionValue(calories,"Calories");
        validateNutritionValue(protein,"Protein");
        validateNutritionValue(carbs,"Carbs");
        validateNutritionValue(fat,"Fat");
        
        //Initialise attributes
        this.foodName = foodName.trim();
        this.mealType = mealType.trim();
        this.quantity = quantity.trim();
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }
    
    //text validation
    private static void validateText(String value, String fieldName){
        if(value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(
            fieldName + " cannot be empty."
            );
        }
    }
    
    //nutrition value validation
    private static void validateNutritionValue(double value, String fieldName){
        if(!Double.isFinite(value) || value < 0){
            throw new IllegalArgumentException(
            fieldName + " cannot be negative or invalid."
            );
        }
    }
    
    //setters
    public void setFoodName(String foodName) {
        validateText(foodName,"Food Name");
        this.foodName = foodName.trim();
    }

    public void setMealType(String mealType) {
        validateText(mealType,"Meal Type");
        this.mealType = mealType.trim();
    }

    public void setQuantity(String quantity) {
        validateText(quantity,"Quantity");
        this.quantity = quantity.trim();
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProtein(double protein) {
        validateNutritionValue(protein,"Protein");
        this.protein = protein;
    }

    public void setCarbs(double carbs) {
        validateNutritionValue(carbs,"Carbs");
        this.carbs = carbs;
    }

    public void setFat(double fat) {
        validateNutritionValue(fat,"Fat");
        this.fat = fat;
    }

    // getters
    public String getFoodName() {
        return foodName;
    }

    public String getMealType() {
        return mealType;
    }

    public String getQuantity() {
        return quantity;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }
    
    // Abstract method
    public abstract String getEntryType();
}
