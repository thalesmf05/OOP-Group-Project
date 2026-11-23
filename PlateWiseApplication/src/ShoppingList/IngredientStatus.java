package ShoppingList;

public class IngredientStatus {
    private String name;
    private double requiredQuantity;
    private double avaliableQuantity;
    private double missingQuantity;
    private String status;

    public IngredientStatus(String name, double requiredQuantity, double avaliableQuantity, double missingQuantity, String status) {
        this.name = name;
        this.requiredQuantity = requiredQuantity;
        this.avaliableQuantity = avaliableQuantity;
        this.missingQuantity = missingQuantity;
        this.status = status;
    }



}
