/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pantry;

/**
 *
 * @author Merlin M
 */
public class NonPerishableItem extends PantryItem { //extends allows inheriting from PantryItem

    // declares local variable/s
    private String category;
    
    //constructor that inherits all of the varaibles from the parent class using the super() method
    public NonPerishableItem(String foodName, String unit, String expiryDate, double quantity) {
        super(foodName, unit, expiryDate, quantity);
    }

    //getter
    public String getCategory() {
        return category;
    }

    //setter
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean isNearExpiry(int days) {
        // overrides the abstract method inherited from PantryItem and always returns false as no check is required.
        return false;
    }

}
