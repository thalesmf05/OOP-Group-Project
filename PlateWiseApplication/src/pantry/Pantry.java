/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pantry;
import java.util.ArrayList; //adds import for arraylist

/**
 *
 * @author Merlin M
 */
public class Pantry implements java.io.Serializable {
    
    //creates an array list that will hold all of the items in the pantry
    private ArrayList<PantryItem> items;

    //constructor
    public Pantry() {
        //inialises the items array
        this.items = new ArrayList<>();
    }
    
    //getter - returns copy of pantry items so other classes can use the list
    public ArrayList<PantryItem> getItems() {
        return items;
    }
    
    // adds a pantry item to the inventory
    public void addItem(PantryItem item) {
        items.add(item);
    }
    
    // removes a pantry item from the inventory
    public void removeItem(PantryItem item) {
        items.remove(item);
    }
    
    // updates the quantity of a pantry item using its index in the ArrayList
    public void updateQuantity(int index, double quantity) {
        items.get(index).setQuantity(quantity);
    }
    
    // creates a method that searches the pantry for items matching the criteria
    public ArrayList<PantryItem> searchItem(String foodName) {
        // creates temp ArrayList that willl store the matching pantry items
        ArrayList<PantryItem> results = new ArrayList<>();
        // loops through every item currently stored in the pantry
        for (PantryItem item : items) {
            // checks if the items food name matches the search term, ignoring case
            if (item.getFoodName().equalsIgnoreCase(foodName)) {
                results.add(item); // adds the matching pantry item to the results ArrayList
            }
        }
        return results;//returns the search results
    }
    
    //creates a method that finds the expiring items
    public ArrayList<PantryItem> getExpiringItems(int days) {
        // creates an ArrayList to store items that are near expiry
        ArrayList<PantryItem> expiringItems = new ArrayList<>();
        // loops through each item currently stored in the pantry
        for (PantryItem item : items) {
            // checks whether the current item is approaching its expiry date
            if (item.isNearExpiry(days)) {
                // adds the item to the expiring items ArrayList
                expiringItems.add(item);
            }
        }
        return expiringItems; // returns the epxiring items
    }

}