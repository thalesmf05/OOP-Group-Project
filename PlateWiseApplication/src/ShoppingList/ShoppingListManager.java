package ShoppingList;

import java.util.ArrayList;

public class ShoppingListManager {
    //VARIABLES
    private ArrayList<ShoppingListItem> items = new ArrayList<>();
    private ArrayList<ShoppingListItem> boughtItems = new ArrayList<>(); //list of items that have been bought this will be handled in the GUI

    //CONSTRUCTOR
    public ShoppingListManager(){
    }

    //GETTERS AND SETTERS
    public ArrayList<ShoppingListItem> getItems(){
        return items;
    }

    public void setBoughtItems(ArrayList<ShoppingListItem> boughtItems) {
        this.boughtItems = boughtItems;
    }

    //SHOPPING LIST MANAGEMENT METHODS
    public void addItem(ShoppingListItem item){
        items.add(item);
    }

    public void removeItem(ShoppingListItem item){
        items.remove(item);
    }

    public void editItem(ShoppingListItem updatedItem){
        items.set(updatedItem.getId(), updatedItem);
    }

    public void removeBoughtItems(){ //removes all the items that have been bought
        items.removeAll(boughtItems);
    }

    public void clearList(){
        items.clear();
    }

    //TODO save and load from file

}
