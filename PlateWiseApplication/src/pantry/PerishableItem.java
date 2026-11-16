/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pantry;
//imports required for this class to aid the calculation of "expiring soon"
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Merlin M
 */
public class PerishableItem extends PantryItem { //extends allows inheriting from PantryItem
    
    //declares local variable/s
    private String storageAdvice;
    
    //constructor calls the parent class using super()
    public PerishableItem(String foodName, String unit, String expiryDate, double quantity) {
        super(foodName, unit, expiryDate, quantity);
    }
    
    //getter
    public String getStorageAdvice() {
        return storageAdvice;
    }
    
    //setter
    public void setStorageAdvice(String storageAdvice) {
        this.storageAdvice = storageAdvice;
    }
    
    @Override
    public boolean isNearExpiry(int days) {
        //creates a new DTF object with the format dd-mm-yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        /*receives the expiry date from parent class(PantryItem), 
        then parses it to convert the String to a proper java date,
        then stores it in a localdate varable named expiry*/
        LocalDate expiry = LocalDate.parse(getExpiryDate(), formatter);
        
        /*creates a local method variable "daysUntilExpiry" 
        then casts the value from long to int returned from ChronoUnit calculation,
        it calculates the number of days between todays date and the expiry date*/
        int daysUntilExpiry = (int) ChronoUnit.DAYS.between(LocalDate.now(), expiry);
        
        // returns true if the item has not expired and expires within the specified number of days, thus we know which items expire soon
        return daysUntilExpiry >= 0 && daysUntilExpiry <= days;
    }

}
