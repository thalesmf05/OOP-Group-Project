/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RecipeManager;

/**
 *
 * @author thale
 */
public class Ingredient {
    private static int nextId = 1; //static variable to keep track of the next id

    private final int id;
    private String name;
    private double requiredAmount;
    private final String unit;

    //constructor for a new ingredient, automatically generates a new id
    public Ingredient(String name, double requiredAmount, String unit){
        this.id = nextId;
        this.name = name;
        this.requiredAmount = requiredAmount;
        this.unit = unit;
        nextId++; //increment the nextId for the next ingredient
    }
    //constructor for an ingredient that already exists and will be edited, this doesn't generate a new id rather sets the old ingredient id
    public Ingredient(int id, String unit, String name, double requiredAmount) {
        this.id = id; //use the same id as the
        this.unit = unit;
        this.name = name;
        this.requiredAmount = requiredAmount;
    }

    public String getName() {
        return name;
    }

    public double getRequiredAmount() {
        return requiredAmount;
    }

    public String getUnit() {
        return unit;
    }

    public int getId(){
        return id;
    }
}
