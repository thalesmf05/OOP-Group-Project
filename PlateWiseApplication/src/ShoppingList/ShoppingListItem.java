package ShoppingList;

public class ShoppingListItem {

    //VARIABLES
    private static int nextId = 1;

    private final int id;
    private String name;
    double quantity;
    String unit;

    //CONSTRUCTOR
    //new shopping list item, automatically generates a new id
    public ShoppingListItem(double quantity, int id, String name, String unit) {
        this.quantity = quantity;
        this.id = nextId;
        this.name = name;
        this.unit = unit;
        nextId++;
    }

    //updated shopping list item, this doesn't generate a new id rather sets the
    // old shopping list item id
    public ShoppingListItem(int id, String name, double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    //GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    //METHODS
    //updates the quantity of the shopping list item when the
    //user wants to add only missing quantities
    public void updateQuantity(double quantity){ //
        this.quantity = quantity;
    }

}
