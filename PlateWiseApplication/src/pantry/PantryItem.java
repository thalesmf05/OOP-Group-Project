/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pantry;

/**
 *
 * @author Merlin M
 */
// abstract parent class for all pantry items, shares attributes and methods
public abstract class PantryItem implements java.io.Serializable {
    
    //delcares variables
    private String foodName, unit, expiryDate;
    private double quantity;

    //constructor
    public PantryItem(String foodName, String unit, String expiryDate, double quantity) {
        this.foodName = foodName;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }
    
    //getters
    public String getFoodName() {
        return foodName;
    }

    public String getUnit() {
        return unit;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    //setters
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    //creates abstact method of isNearExpiry, which subclasses classes must implement and override
    public abstract boolean isNearExpiry(int days);
    
    // allows the user the current quantity that is stored in the pantry
    public void updateQuantity(double amount) {
    this.quantity = this.quantity + amount;
    }
    
}
