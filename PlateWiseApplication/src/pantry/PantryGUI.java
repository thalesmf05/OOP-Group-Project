/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pantry;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Merlin
 */
public class PantryGUI extends javax.swing.JPanel {

    // creates a Pantry object to manage the pantry inventory
    private Pantry pantryInventory = new Pantry();

    public PantryGUI() {
        initComponents();//initialises the gui components
        setupButtonListeners();
        updateExpiryDateField();
        loadEditItemDropdown();
        updateEditExpiryDateField();
    }

    // sets up listeners for the buttons
    private void setupButtonListeners() {

        // creates a listener for text field changes
        DocumentListener listener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkRequiredFields();
                checkSearchField();
                checkEditFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkRequiredFields();
                checkSearchField();
                checkEditFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkRequiredFields();
                checkSearchField();
                checkEditFields();
            }
        };

        // add item form
        foodNameTf.getDocument().addDocumentListener(listener);
        quantityTf.getDocument().addDocumentListener(listener);
        expiryDateTf.getDocument().addDocumentListener(listener);

        // search field
        searchTf.getDocument().addDocumentListener(listener);

        // edit item form
        foodNameTf1.getDocument().addDocumentListener(listener);
        quantityTf1.getDocument().addDocumentListener(listener);
        expiryDateTf1.getDocument().addDocumentListener(listener);

        // add item dropdowns
        itemTypeCb.addActionListener(e -> {
            updateExpiryDateField();
            checkRequiredFields();
        });
        unitCb.addActionListener(e -> checkRequiredFields());

        // edit item dropdowns
        itemTypeCb1.addActionListener(e -> {
            updateEditExpiryDateField();
            checkEditFields();
        });
        unitCb1.addActionListener(e -> checkEditFields());

        // edit item selection
        selectItemCb.addActionListener(e -> populateEditForm());

        // checks buttons when the gui opens
        checkRequiredFields();
        checkSearchField();
        checkEditFields();
    }

    // loads pantry items into the edit item dropdown
    private void loadEditItemDropdown() {

        // clears the existing dropdown items
        selectItemCb.removeAllItems();

        // adds the default selection
        selectItemCb.addItem("Select an item...");

        // loops through the pantry inventory
        for (PantryItem item : pantryInventory.getItems()) {
            // adds each pantry item's name to the dropdown
            selectItemCb.addItem(item.getFoodName());
        }
    }

    // populates the edit form with the selected pantry item
    private void populateEditForm() {

        // gets the selected item index
        int selectedIndex = selectItemCb.getSelectedIndex();

        // checks whether an actual item has been selected
        if (selectedIndex <= 0) {
            return;
        }

        // gets the selected pantry item from the inventory
        PantryItem item = pantryInventory.getItems().get(selectedIndex - 1);

        // populates the common fields
        foodNameTf1.setText(item.getFoodName());
        quantityTf1.setText(String.valueOf(item.getQuantity()));
        expiryDateTf1.setText(item.getExpiryDate());

        // selects the item's unit
        unitCb1.setSelectedItem(item.getUnit());

        // checks whether the item is perishable
        if (item instanceof PerishableItem) {

            PerishableItem perishableItem = (PerishableItem) item;

            itemTypeCb1.setSelectedItem("1. Perishable");
            storageAdviceTf1.setText(perishableItem.getStorageAdvice());
            categoryTf1.setText("");

        } else if (item instanceof NonPerishableItem) {

            NonPerishableItem nonPerishableItem = (NonPerishableItem) item;

            itemTypeCb1.setSelectedItem("2. Non-Perishable");
            categoryTf1.setText(nonPerishableItem.getCategory());
            storageAdviceTf1.setText("");
        }

        // checks whether the update button can be enabled
        updateEditExpiryDateField();
        checkEditFields();
    }

    // enables or disables the expiry date field based on item type
    private void updateExpiryDateField() {

        String itemType = itemTypeCb.getSelectedItem().toString();

        // checks whether the item is perishable
        if (itemType.equals("1. Perishable")) {
            expiryDateTf.setEnabled(true);
        } else {
            expiryDateTf.setText("");
            expiryDateTf.setEnabled(false);
        }
    }

    // Checks whether the required Add Item fields are completed
    private void checkRequiredFields() {
        // Retrieves the values from the Add Item form
        String foodName = foodNameTf.getText().trim();
        String quantityText = quantityTf.getText().trim();
        String expiryDate = expiryDateTf.getText().trim();
        String itemType = itemTypeCb.getSelectedItem().toString();
        String unit = unitCb.getSelectedItem().toString();

        // Checks the basic required fields
        boolean validFields = !foodName.isEmpty()
                && !foodName.equals("e.g. Chicken, rice, milk")
                && !itemType.equals("Select Item Type...")
                && !unit.equals("Select Unit...");

        // checks the expiry date only for perishable items
        if (itemType.equals("1. Perishable")) {
            validFields = validFields
                    && !expiryDate.isEmpty()
                    && !expiryDate.equals("DD-MM-YYYY");
        }

        // Checks whether the quantity is a valid positive number
        try {
            double quantity = Double.parseDouble(quantityText);

            if (quantity <= 0) {
                validFields = false;
            }
        } catch (NumberFormatException e) {
            validFields = false;
        }

        // checks whether the expiry date is valid for perishable items
        if (itemType.equals("1. Perishable")) {
            try {
                java.time.LocalDate.parse(
                        expiryDate,
                        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                );
            } catch (java.time.format.DateTimeParseException e) {
                validFields = false;
            }
        }
        // Enables or disables the Save Item button
        saveItemBtn.setEnabled(validFields);
    }

    // enables or disables the edit expiry date field based on item type
    private void updateEditExpiryDateField() {

        // gets the currently selected item type from the dropdown
        String itemType = itemTypeCb1.getSelectedItem().toString();

        // checks whether the selected item is perishable
        if (itemType.equals("1. Perishable")) {

            // enables the expiry date field for perishable items
            expiryDateTf1.setEnabled(true);

            // enables the storage advice field and its label
            storageAdviceTf1.setEnabled(true);
            storageAdviceLbl1.setEnabled(true);

            // disables the category field and its label
            categoryTf1.setEnabled(false);
            categoryLbl1.setEnabled(false);

        } // checks whether the selected item is non-perishable
        else if (itemType.equals("2. Non-Perishable")) {

            // clears and disables the expiry date field
            expiryDateTf1.setText("");
            expiryDateTf1.setEnabled(false);

            // clears and disables the storage advice field and its label
            storageAdviceTf1.setText("");
            storageAdviceTf1.setEnabled(false);
            storageAdviceLbl1.setEnabled(false);

            // enables the category field and its label
            categoryTf1.setEnabled(true);
            categoryLbl1.setEnabled(true);

        } // handles the case where no item type has been selected
        else {

            // disables the expiry date field
            expiryDateTf1.setEnabled(false);

            // disables the storage advice field and its label
            storageAdviceTf1.setEnabled(false);
            storageAdviceLbl1.setEnabled(false);

            // disables the category field and its label
            categoryTf1.setEnabled(false);
            categoryLbl1.setEnabled(false);
        }
    }

    // checks whether the search field contains text
    private void checkSearchField() {

        // gets the search text
        String searchText = searchTf.getText().trim();

        // checks whether the search field is empty
        boolean validSearch = !searchText.isEmpty()
                && !searchText.equals("Search pantry items...");

        // enables or disables the search button
        searchBtn.setEnabled(validSearch);
    }

    // checks whether the Edit Item fields are completed
    private void checkEditFields() {

        // gets the values from the Edit Item form
        String foodName = foodNameTf1.getText().trim();
        String quantityText = quantityTf1.getText().trim();
        String expiryDate = expiryDateTf1.getText().trim();

        String itemType = itemTypeCb1.getSelectedItem().toString();
        String unit = unitCb1.getSelectedItem().toString();

        // checks the basic required fields
        boolean validFields = selectItemCb.getSelectedIndex() > 0
                && !foodName.isEmpty()
                && !foodName.equals("e.g. Chicken, rice, milk")
                && !expiryDate.isEmpty()
                && !expiryDate.equals("DD-MM-YYYY")
                && !itemType.equals("Select Item Type...")
                && !unit.equals("Select Unit...");

        // checks whether the quantity is a valid positive number
        try {
            double quantity = Double.parseDouble(quantityText);

            if (quantity <= 0) {
                validFields = false;
            }

        } catch (NumberFormatException e) {
            validFields = false;
        }

        // checks whether the expiry date is valid
        try {
            java.time.LocalDate.parse(
                    expiryDate,
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
            );

        } catch (java.time.format.DateTimeParseException e) {
            validFields = false;
        }

        // checks the additional fields for each item type
        if (itemType.equals("1. Perishable")) {

            if (storageAdviceTf1.getText().trim().isEmpty()) {
                validFields = false;
            }

        } else if (itemType.equals("2. Non-Perishable")) {

            if (categoryTf1.getText().trim().isEmpty()) {
                validFields = false;
            }
        }

        // enables or disables the Update Item button
        updateItemBtn.setEnabled(validFields);
    }

    // refreshes the pantry inventory table
    private void refreshInventoryTable() {

        //gets the table model
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) inventoryTbl.getModel();

        //clears the existing table rows
        model.setRowCount(0);

        //loops through all pantry items
        for (PantryItem item : pantryInventory.getItems()) {
            //determines the item type
            String itemType;

            if (item instanceof PerishableItem) {
                itemType = "Perishable";
            } else {
                itemType = "Non-Perishable";
            }

            //adds the item details to the table
            model.addRow(new Object[]{
                item.getFoodName(),
                item.getQuantity(),
                item.getUnit(),
                item.getExpiryDate(),
                itemType
            });
        }

    }

    // updates the pantry statistics
    private void updatePantryStatistics() {

        //gets the total number of pantry items
        int totalItems = pantryInventory.getItems().size();

        //creates var to store the count of perishable items
        int perishableItems = 0;

        for (PantryItem item : pantryInventory.getItems()) {
            if (item instanceof PerishableItem) {
                perishableItems++;
            }
        }
        //gets the number of items expiring soon
        int expiringItems = pantryInventory.getExpiringItems(2).size();

        //updates the stat labels
        totalItemsValueLbl.setText(String.valueOf(totalItems));
        perishableValueLbl.setText(String.valueOf(perishableItems));
        expiringValueLbl.setText(String.valueOf(expiringItems));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pantryTitleLbl = new javax.swing.JLabel();
        pantryDescLbl = new javax.swing.JLabel();
        totalItemsPnl = new javax.swing.JPanel();
        totalItemsValueLbl = new javax.swing.JLabel();
        totalItemsTitleLbl = new javax.swing.JLabel();
        perishablePnl = new javax.swing.JPanel();
        perishableValueLbl = new javax.swing.JLabel();
        perishableTitleLbl = new javax.swing.JLabel();
        expiringPnl = new javax.swing.JPanel();
        expiringValueLbl = new javax.swing.JLabel();
        expiringTitleLbl = new javax.swing.JLabel();
        pantryTabbedPn = new javax.swing.JTabbedPane();
        inventoryPnl = new javax.swing.JPanel();
        searchTf = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTbl = new javax.swing.JTable();
        expiringItemsBtn = new javax.swing.JButton();
        addItemPnl = new javax.swing.JPanel();
        foodNameRowPnl = new javax.swing.JPanel();
        foodNameLbl = new javax.swing.JLabel();
        foodNameTf = new javax.swing.JTextField();
        storageAdviceRowPnl = new javax.swing.JPanel();
        storageAdviceLbl = new javax.swing.JLabel();
        storageAdviceTf = new javax.swing.JTextField();
        itemTypeRowPnl = new javax.swing.JPanel();
        itemTypeLbl = new javax.swing.JLabel();
        itemTypeCb = new javax.swing.JComboBox<>();
        categoryRowPnl = new javax.swing.JPanel();
        categoryTf = new javax.swing.JTextField();
        categoryLbl = new javax.swing.JLabel();
        quantityRowPnl = new javax.swing.JPanel();
        quantityLbl = new javax.swing.JLabel();
        quantityTf = new javax.swing.JTextField();
        unitRowPnl = new javax.swing.JPanel();
        unitLbl = new javax.swing.JLabel();
        unitCb = new javax.swing.JComboBox<>();
        expiryDateRowPnl = new javax.swing.JPanel();
        expiryDateLbl = new javax.swing.JLabel();
        expiryDateTf = new javax.swing.JTextField();
        saveItemBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        editItemPnl = new javax.swing.JPanel();
        selectItemRowPnl = new javax.swing.JPanel();
        selectItemLbl = new javax.swing.JLabel();
        selectItemCb = new javax.swing.JComboBox<>();
        editButtonsPnl = new javax.swing.JPanel();
        updateItemBtn = new javax.swing.JButton();
        clearEditBtn = new javax.swing.JButton();
        deleteItemBtn = new javax.swing.JButton();
        categoryRowPnl1 = new javax.swing.JPanel();
        categoryTf1 = new javax.swing.JTextField();
        categoryLbl1 = new javax.swing.JLabel();
        storageAdviceRowPnl1 = new javax.swing.JPanel();
        storageAdviceLbl1 = new javax.swing.JLabel();
        storageAdviceTf1 = new javax.swing.JTextField();
        itemTypeRowPnl1 = new javax.swing.JPanel();
        itemTypeLbl1 = new javax.swing.JLabel();
        itemTypeCb1 = new javax.swing.JComboBox<>();
        expiryDateRowPnl1 = new javax.swing.JPanel();
        expiryDateLbl1 = new javax.swing.JLabel();
        expiryDateTf1 = new javax.swing.JTextField();
        unitRowPnl1 = new javax.swing.JPanel();
        unitLbl1 = new javax.swing.JLabel();
        unitCb1 = new javax.swing.JComboBox<>();
        quantityRowPnl1 = new javax.swing.JPanel();
        quantityLbl1 = new javax.swing.JLabel();
        quantityTf1 = new javax.swing.JTextField();
        foodNameRowPnl1 = new javax.swing.JPanel();
        foodNameLbl1 = new javax.swing.JLabel();
        foodNameTf1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(23, 40, 59));
        setPreferredSize(new java.awt.Dimension(1000, 750));

        pantryTitleLbl.setBackground(new java.awt.Color(255, 255, 255));
        pantryTitleLbl.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        pantryTitleLbl.setForeground(new java.awt.Color(255, 255, 255));
        pantryTitleLbl.setText("Pantry");

        pantryDescLbl.setBackground(new java.awt.Color(200, 212, 225));
        pantryDescLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        pantryDescLbl.setForeground(new java.awt.Color(190, 200, 215));
        pantryDescLbl.setText("Manage your food inventory, quantities and expiry dates.");

        totalItemsPnl.setBackground(new java.awt.Color(18, 59, 59));
        totalItemsPnl.setMinimumSize(new java.awt.Dimension(100, 90));
        totalItemsPnl.setPreferredSize(new java.awt.Dimension(210, 90));

        totalItemsValueLbl.setBackground(new java.awt.Color(200, 212, 225));
        totalItemsValueLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        totalItemsValueLbl.setForeground(new java.awt.Color(255, 255, 255));
        totalItemsValueLbl.setText("30");

        totalItemsTitleLbl.setBackground(new java.awt.Color(200, 212, 225));
        totalItemsTitleLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        totalItemsTitleLbl.setForeground(new java.awt.Color(255, 255, 255));
        totalItemsTitleLbl.setText("Total Items");

        javax.swing.GroupLayout totalItemsPnlLayout = new javax.swing.GroupLayout(totalItemsPnl);
        totalItemsPnl.setLayout(totalItemsPnlLayout);
        totalItemsPnlLayout.setHorizontalGroup(
            totalItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalItemsPnlLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(totalItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalItemsTitleLbl)
                    .addComponent(totalItemsValueLbl))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        totalItemsPnlLayout.setVerticalGroup(
            totalItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalItemsPnlLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(totalItemsValueLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalItemsTitleLbl)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        perishablePnl.setBackground(new java.awt.Color(18, 50, 74));
        perishablePnl.setPreferredSize(new java.awt.Dimension(210, 90));

        perishableValueLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        perishableValueLbl.setForeground(new java.awt.Color(255, 255, 255));
        perishableValueLbl.setText("8");

        perishableTitleLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        perishableTitleLbl.setForeground(new java.awt.Color(255, 255, 255));
        perishableTitleLbl.setText("Perishable Items");

        javax.swing.GroupLayout perishablePnlLayout = new javax.swing.GroupLayout(perishablePnl);
        perishablePnl.setLayout(perishablePnlLayout);
        perishablePnlLayout.setHorizontalGroup(
            perishablePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perishablePnlLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(perishablePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(perishableTitleLbl)
                    .addComponent(perishableValueLbl))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        perishablePnlLayout.setVerticalGroup(
            perishablePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perishablePnlLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(perishableValueLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(perishableTitleLbl)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        expiringPnl.setBackground(new java.awt.Color(41, 36, 64));
        expiringPnl.setPreferredSize(new java.awt.Dimension(210, 90));

        expiringValueLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        expiringValueLbl.setForeground(new java.awt.Color(255, 255, 255));
        expiringValueLbl.setText("2");

        expiringTitleLbl.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        expiringTitleLbl.setForeground(new java.awt.Color(255, 255, 255));
        expiringTitleLbl.setText("Expiring Soon");

        javax.swing.GroupLayout expiringPnlLayout = new javax.swing.GroupLayout(expiringPnl);
        expiringPnl.setLayout(expiringPnlLayout);
        expiringPnlLayout.setHorizontalGroup(
            expiringPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiringPnlLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(expiringPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expiringTitleLbl)
                    .addComponent(expiringValueLbl))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        expiringPnlLayout.setVerticalGroup(
            expiringPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiringPnlLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(expiringValueLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expiringTitleLbl)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pantryTabbedPn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        pantryTabbedPn.setName(""); // NOI18N
        pantryTabbedPn.setPreferredSize(new java.awt.Dimension(350, 35));

        searchTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        searchTf.setText("Search pantry items...");
        searchTf.addActionListener(this::searchTfActionPerformed);

        searchBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        searchBtn.setText("Search");
        searchBtn.setEnabled(false);
        searchBtn.addActionListener(this::searchBtnActionPerformed);

        inventoryTbl.setBackground(new java.awt.Color(23, 40, 59));
        inventoryTbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        inventoryTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Food Name", "Quantity", "Unit", "Expiry Date", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTbl.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(inventoryTbl);
        inventoryTbl.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        expiringItemsBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        expiringItemsBtn.setText("Show Expiring Items");
        expiringItemsBtn.addActionListener(this::expiringItemsBtnActionPerformed);

        javax.swing.GroupLayout inventoryPnlLayout = new javax.swing.GroupLayout(inventoryPnl);
        inventoryPnl.setLayout(inventoryPnlLayout);
        inventoryPnlLayout.setHorizontalGroup(
            inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPnlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiringItemsBtn)
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addComponent(searchTf, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchBtn)))
                .addContainerGap(147, Short.MAX_VALUE))
        );
        inventoryPnlLayout.setVerticalGroup(
            inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchBtn)
                    .addComponent(searchTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(expiringItemsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pantryTabbedPn.addTab("Inventory", inventoryPnl);

        foodNameRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        foodNameLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        foodNameLbl.setText("Food Name:");
        foodNameLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        foodNameLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        foodNameLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        foodNameTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        foodNameTf.setText("e.g. Chicken, rice, milk");
        foodNameTf.setPreferredSize(new java.awt.Dimension(350, 35));

        javax.swing.GroupLayout foodNameRowPnlLayout = new javax.swing.GroupLayout(foodNameRowPnl);
        foodNameRowPnl.setLayout(foodNameRowPnlLayout);
        foodNameRowPnlLayout.setHorizontalGroup(
            foodNameRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodNameRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        foodNameRowPnlLayout.setVerticalGroup(
            foodNameRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodNameRowPnlLayout.createSequentialGroup()
                .addGroup(foodNameRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(foodNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        storageAdviceRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        storageAdviceLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        storageAdviceLbl.setText("Storage Advice:");
        storageAdviceLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        storageAdviceLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        storageAdviceLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        storageAdviceTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        storageAdviceTf.setText("e.g Keep refridgerated");
        storageAdviceTf.setPreferredSize(new java.awt.Dimension(350, 35));

        javax.swing.GroupLayout storageAdviceRowPnlLayout = new javax.swing.GroupLayout(storageAdviceRowPnl);
        storageAdviceRowPnl.setLayout(storageAdviceRowPnlLayout);
        storageAdviceRowPnlLayout.setHorizontalGroup(
            storageAdviceRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storageAdviceRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(storageAdviceLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(storageAdviceTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        storageAdviceRowPnlLayout.setVerticalGroup(
            storageAdviceRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storageAdviceRowPnlLayout.createSequentialGroup()
                .addGroup(storageAdviceRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(storageAdviceTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storageAdviceLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        itemTypeRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        itemTypeLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        itemTypeLbl.setText("Item Type:");
        itemTypeLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        itemTypeLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        itemTypeLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        itemTypeCb.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        itemTypeCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Item Type...", "1. Perishable", "2. Non-Perishable" }));
        itemTypeCb.setPreferredSize(new java.awt.Dimension(350, 35));

        javax.swing.GroupLayout itemTypeRowPnlLayout = new javax.swing.GroupLayout(itemTypeRowPnl);
        itemTypeRowPnl.setLayout(itemTypeRowPnlLayout);
        itemTypeRowPnlLayout.setHorizontalGroup(
            itemTypeRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTypeRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemTypeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTypeCb, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(348, Short.MAX_VALUE))
        );
        itemTypeRowPnlLayout.setVerticalGroup(
            itemTypeRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTypeRowPnlLayout.createSequentialGroup()
                .addGroup(itemTypeRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemTypeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemTypeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        categoryTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        categoryTf.setText("e.g. Canned goods, grains");
        categoryTf.setPreferredSize(new java.awt.Dimension(350, 35));
        categoryTf.addActionListener(this::categoryTfActionPerformed);

        categoryLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        categoryLbl.setText("Category:");
        categoryLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        categoryLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        categoryLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        javax.swing.GroupLayout categoryRowPnlLayout = new javax.swing.GroupLayout(categoryRowPnl);
        categoryRowPnl.setLayout(categoryRowPnlLayout);
        categoryRowPnlLayout.setHorizontalGroup(
            categoryRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(categoryLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(346, Short.MAX_VALUE))
        );
        categoryRowPnlLayout.setVerticalGroup(
            categoryRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryRowPnlLayout.createSequentialGroup()
                .addGroup(categoryRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoryTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        quantityRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        quantityLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        quantityLbl.setText("Quantity:");
        quantityLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        quantityLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        quantityLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        quantityTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        quantityTf.setText("e.g. 2");
        quantityTf.setPreferredSize(new java.awt.Dimension(350, 35));
        quantityTf.addActionListener(this::quantityTfActionPerformed);

        javax.swing.GroupLayout quantityRowPnlLayout = new javax.swing.GroupLayout(quantityRowPnl);
        quantityRowPnl.setLayout(quantityRowPnlLayout);
        quantityRowPnlLayout.setHorizontalGroup(
            quantityRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quantityRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quantityLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quantityTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        quantityRowPnlLayout.setVerticalGroup(
            quantityRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quantityRowPnlLayout.createSequentialGroup()
                .addGroup(quantityRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quantityTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        unitRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        unitLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        unitLbl.setText("Unit:");
        unitLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        unitLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        unitLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        unitCb.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        unitCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Unit...", "1. Kg", "2. Grams", "3. Litres", "4. Millilitres", "5. Units", "6. Other" }));
        unitCb.setMinimumSize(new java.awt.Dimension(350, 35));
        unitCb.setPreferredSize(new java.awt.Dimension(350, 35));
        unitCb.addActionListener(this::unitCbActionPerformed);

        javax.swing.GroupLayout unitRowPnlLayout = new javax.swing.GroupLayout(unitRowPnl);
        unitRowPnl.setLayout(unitRowPnlLayout);
        unitRowPnlLayout.setHorizontalGroup(
            unitRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(unitRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unitLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitCb, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        unitRowPnlLayout.setVerticalGroup(
            unitRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, unitRowPnlLayout.createSequentialGroup()
                .addGroup(unitRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        expiryDateRowPnl.setPreferredSize(new java.awt.Dimension(700, 45));

        expiryDateLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        expiryDateLbl.setText("Expiry Date:");
        expiryDateLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        expiryDateLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        expiryDateLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        expiryDateTf.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        expiryDateTf.setText("DD-MM-YYYY");
        expiryDateTf.setPreferredSize(new java.awt.Dimension(350, 35));

        javax.swing.GroupLayout expiryDateRowPnlLayout = new javax.swing.GroupLayout(expiryDateRowPnl);
        expiryDateRowPnl.setLayout(expiryDateRowPnlLayout);
        expiryDateRowPnlLayout.setHorizontalGroup(
            expiryDateRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiryDateRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expiryDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expiryDateTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        expiryDateRowPnlLayout.setVerticalGroup(
            expiryDateRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiryDateRowPnlLayout.createSequentialGroup()
                .addGroup(expiryDateRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expiryDateTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiryDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        saveItemBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        saveItemBtn.setText("Save");
        saveItemBtn.setEnabled(false);
        saveItemBtn.addActionListener(this::saveItemBtnActionPerformed);

        clearBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        clearBtn.setText("Clear");
        clearBtn.addActionListener(this::clearBtnActionPerformed);

        javax.swing.GroupLayout addItemPnlLayout = new javax.swing.GroupLayout(addItemPnl);
        addItemPnl.setLayout(addItemPnlLayout);
        addItemPnlLayout.setHorizontalGroup(
            addItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addItemPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(storageAdviceRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(itemTypeRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(expiryDateRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(unitRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(quantityRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(foodNameRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addGroup(addItemPnlLayout.createSequentialGroup()
                        .addGroup(addItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(categoryRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addItemPnlLayout.createSequentialGroup()
                                .addComponent(saveItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addItemPnlLayout.setVerticalGroup(
            addItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addItemPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodNameRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quantityRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expiryDateRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTypeRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(storageAdviceRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(addItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pantryTabbedPn.addTab("Add Item", addItemPnl);

        selectItemLbl.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        selectItemLbl.setText("Select Item:");
        selectItemLbl.setMaximumSize(new java.awt.Dimension(130, 30));
        selectItemLbl.setMinimumSize(new java.awt.Dimension(130, 30));
        selectItemLbl.setPreferredSize(new java.awt.Dimension(130, 30));

        selectItemCb.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        selectItemCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select an item..." }));
        selectItemCb.setName(""); // NOI18N
        selectItemCb.setPreferredSize(new java.awt.Dimension(300, 35));

        javax.swing.GroupLayout selectItemRowPnlLayout = new javax.swing.GroupLayout(selectItemRowPnl);
        selectItemRowPnl.setLayout(selectItemRowPnlLayout);
        selectItemRowPnlLayout.setHorizontalGroup(
            selectItemRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectItemRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectItemLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectItemCb, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        selectItemRowPnlLayout.setVerticalGroup(
            selectItemRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectItemRowPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectItemRowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectItemLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectItemCb, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updateItemBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        updateItemBtn.setText("Update Item");
        updateItemBtn.setEnabled(false);
        updateItemBtn.addActionListener(this::updateItemBtnActionPerformed);

        clearEditBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        clearEditBtn.setText("Clear");
        clearEditBtn.addActionListener(this::clearEditBtnActionPerformed);

        deleteItemBtn.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        deleteItemBtn.setText("Delete Item");
        deleteItemBtn.setEnabled(false);
        deleteItemBtn.addActionListener(this::deleteItemBtnActionPerformed);

        javax.swing.GroupLayout editButtonsPnlLayout = new javax.swing.GroupLayout(editButtonsPnl);
        editButtonsPnl.setLayout(editButtonsPnlLayout);
        editButtonsPnlLayout.setHorizontalGroup(
            editButtonsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editButtonsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clearEditBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editButtonsPnlLayout.setVerticalGroup(
            editButtonsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editButtonsPnlLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(editButtonsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearEditBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        categoryRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        categoryTf1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        categoryTf1.setText("e.g. Canned goods, grains");
        categoryTf1.setPreferredSize(new java.awt.Dimension(300, 35));
        categoryTf1.addActionListener(this::categoryTf1ActionPerformed);

        categoryLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        categoryLbl1.setText("Category:");
        categoryLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        categoryLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        categoryLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        javax.swing.GroupLayout categoryRowPnl1Layout = new javax.swing.GroupLayout(categoryRowPnl1);
        categoryRowPnl1.setLayout(categoryRowPnl1Layout);
        categoryRowPnl1Layout.setHorizontalGroup(
            categoryRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(categoryLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        categoryRowPnl1Layout.setVerticalGroup(
            categoryRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryRowPnl1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(categoryRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoryLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categoryTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        storageAdviceRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        storageAdviceLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        storageAdviceLbl1.setText("Storage Advice:");
        storageAdviceLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        storageAdviceLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        storageAdviceLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        storageAdviceTf1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        storageAdviceTf1.setText("e.g Keep refridgerated");
        storageAdviceTf1.setPreferredSize(new java.awt.Dimension(300, 35));

        javax.swing.GroupLayout storageAdviceRowPnl1Layout = new javax.swing.GroupLayout(storageAdviceRowPnl1);
        storageAdviceRowPnl1.setLayout(storageAdviceRowPnl1Layout);
        storageAdviceRowPnl1Layout.setHorizontalGroup(
            storageAdviceRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storageAdviceRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(storageAdviceLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(storageAdviceTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 222, Short.MAX_VALUE))
        );
        storageAdviceRowPnl1Layout.setVerticalGroup(
            storageAdviceRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storageAdviceRowPnl1Layout.createSequentialGroup()
                .addGroup(storageAdviceRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(storageAdviceTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storageAdviceLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        itemTypeRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        itemTypeLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        itemTypeLbl1.setText("Item Type:");
        itemTypeLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        itemTypeLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        itemTypeLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        itemTypeCb1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        itemTypeCb1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Item Type...", "1. Perishable", "2. Non-Perishable" }));
        itemTypeCb1.setPreferredSize(new java.awt.Dimension(300, 35));

        javax.swing.GroupLayout itemTypeRowPnl1Layout = new javax.swing.GroupLayout(itemTypeRowPnl1);
        itemTypeRowPnl1.setLayout(itemTypeRowPnl1Layout);
        itemTypeRowPnl1Layout.setHorizontalGroup(
            itemTypeRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTypeRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemTypeLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTypeCb1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        itemTypeRowPnl1Layout.setVerticalGroup(
            itemTypeRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemTypeRowPnl1Layout.createSequentialGroup()
                .addGroup(itemTypeRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemTypeCb1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemTypeLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        expiryDateRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        expiryDateLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        expiryDateLbl1.setText("Expiry Date:");
        expiryDateLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        expiryDateLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        expiryDateLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        expiryDateTf1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        expiryDateTf1.setText("DD-MM-YYYY");
        expiryDateTf1.setPreferredSize(new java.awt.Dimension(300, 35));

        javax.swing.GroupLayout expiryDateRowPnl1Layout = new javax.swing.GroupLayout(expiryDateRowPnl1);
        expiryDateRowPnl1.setLayout(expiryDateRowPnl1Layout);
        expiryDateRowPnl1Layout.setHorizontalGroup(
            expiryDateRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiryDateRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expiryDateLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expiryDateTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        expiryDateRowPnl1Layout.setVerticalGroup(
            expiryDateRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expiryDateRowPnl1Layout.createSequentialGroup()
                .addGroup(expiryDateRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expiryDateTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiryDateLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        unitRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        unitLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        unitLbl1.setText("Unit:");
        unitLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        unitLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        unitLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        unitCb1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        unitCb1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Unit...", "1. Kg", "2. Grams", "3. Litres", "4. Millilitres", "5. Units", "6. Other" }));
        unitCb1.setMinimumSize(new java.awt.Dimension(350, 35));
        unitCb1.setPreferredSize(new java.awt.Dimension(300, 35));
        unitCb1.addActionListener(this::unitCb1ActionPerformed);

        javax.swing.GroupLayout unitRowPnl1Layout = new javax.swing.GroupLayout(unitRowPnl1);
        unitRowPnl1.setLayout(unitRowPnl1Layout);
        unitRowPnl1Layout.setHorizontalGroup(
            unitRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(unitRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unitLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitCb1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        unitRowPnl1Layout.setVerticalGroup(
            unitRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(unitRowPnl1Layout.createSequentialGroup()
                .addGroup(unitRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitCb1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        quantityRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        quantityLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        quantityLbl1.setText("Quantity:");
        quantityLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        quantityLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        quantityLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        quantityTf1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        quantityTf1.setText("e.g. 2");
        quantityTf1.setPreferredSize(new java.awt.Dimension(300, 35));
        quantityTf1.addActionListener(this::quantityTf1ActionPerformed);

        javax.swing.GroupLayout quantityRowPnl1Layout = new javax.swing.GroupLayout(quantityRowPnl1);
        quantityRowPnl1.setLayout(quantityRowPnl1Layout);
        quantityRowPnl1Layout.setHorizontalGroup(
            quantityRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quantityRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quantityLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quantityTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        quantityRowPnl1Layout.setVerticalGroup(
            quantityRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quantityRowPnl1Layout.createSequentialGroup()
                .addGroup(quantityRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantityTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        foodNameRowPnl1.setPreferredSize(new java.awt.Dimension(700, 35));

        foodNameLbl1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        foodNameLbl1.setText("Food Name:");
        foodNameLbl1.setMaximumSize(new java.awt.Dimension(130, 30));
        foodNameLbl1.setMinimumSize(new java.awt.Dimension(130, 30));
        foodNameLbl1.setPreferredSize(new java.awt.Dimension(130, 30));

        foodNameTf1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        foodNameTf1.setText("e.g. Chicken, rice, milk");
        foodNameTf1.setPreferredSize(new java.awt.Dimension(300, 35));

        javax.swing.GroupLayout foodNameRowPnl1Layout = new javax.swing.GroupLayout(foodNameRowPnl1);
        foodNameRowPnl1.setLayout(foodNameRowPnl1Layout);
        foodNameRowPnl1Layout.setHorizontalGroup(
            foodNameRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodNameRowPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodNameLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodNameTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );
        foodNameRowPnl1Layout.setVerticalGroup(
            foodNameRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodNameRowPnl1Layout.createSequentialGroup()
                .addGroup(foodNameRowPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodNameTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(foodNameLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout editItemPnlLayout = new javax.swing.GroupLayout(editItemPnl);
        editItemPnl.setLayout(editItemPnlLayout);
        editItemPnlLayout.setHorizontalGroup(
            editItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editItemPnlLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(editItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editItemPnlLayout.createSequentialGroup()
                        .addComponent(selectItemRowPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(editItemPnlLayout.createSequentialGroup()
                        .addGroup(editItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(foodNameRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantityRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(expiryDateRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemTypeRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(storageAdviceRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoryRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))))
            .addGroup(editItemPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editButtonsPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        editItemPnlLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {categoryRowPnl1, expiryDateRowPnl1, foodNameRowPnl1, itemTypeRowPnl1, quantityRowPnl1, storageAdviceRowPnl1, unitRowPnl1});

        editItemPnlLayout.setVerticalGroup(
            editItemPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editItemPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectItemRowPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodNameRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quantityRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expiryDateRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTypeRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(storageAdviceRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryRowPnl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editButtonsPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pantryTabbedPn.addTab("Edit item", editItemPnl);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pantryDescLbl)
                    .addComponent(pantryTitleLbl)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(totalItemsPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(49, 49, 49)
                            .addComponent(perishablePnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(expiringPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(pantryTabbedPn, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(241, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pantryTitleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(pantryDescLbl)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalItemsPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(perishablePnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiringPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(pantryTabbedPn, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void searchTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTfActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed

        // gets the search text
        String searchText = searchTf.getText().trim().toLowerCase();

        // gets the table model
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) inventoryTbl.getModel();

        // clears the existing table rows
        model.setRowCount(0);

        // stores whether a matching item was found
        boolean itemFound = false;

        // searches through the pantry inventory
        for (PantryItem item : pantryInventory.getItems()) {

            // checks whether the food name contains the search text
            if (item.getFoodName().toLowerCase().contains(searchText)) {

                // determines the item type
                String itemType;

                if (item instanceof PerishableItem) {
                    itemType = "Perishable";
                } else {
                    itemType = "Non-Perishable";
                }

                // adds the matching item to the table
                model.addRow(new Object[]{
                    item.getFoodName(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getExpiryDate(),
                    itemType
                });

                itemFound = true;
            }
        }

        // informs the user if no matching items were found
        if (!itemFound) {
            JOptionPane.showMessageDialog(this,
                    "No matching pantry items were found.");
        }


    }//GEN-LAST:event_searchBtnActionPerformed

    private void categoryTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryTfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryTfActionPerformed

    private void quantityTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTfActionPerformed

    private void unitCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitCbActionPerformed

    private void categoryTf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryTf1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryTf1ActionPerformed

    private void unitCb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitCb1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitCb1ActionPerformed

    private void quantityTf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTf1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTf1ActionPerformed

    private void saveItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveItemBtnActionPerformed
        // retrieves the values entered into the Add Item form
        String foodName = foodNameTf.getText().trim();
        String unit = unitCb.getSelectedItem().toString();
        String expiryDate = expiryDateTf.getText().trim();
        String itemType = itemTypeCb.getSelectedItem().toString();
        String storageAdvice = storageAdviceTf.getText().trim();
        String category = categoryTf.getText().trim();

        // checks that the required fields are not empty
        if (foodName.isEmpty() || quantityTf.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all required fields.");
            return;
        }

        // converts the quantity from a String into a double
        double quantity;

        try {
            quantity = Double.parseDouble(quantityTf.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            return;
        }

        // creates a PantryItem object based on the selected item type
        PantryItem newItem = null;

        // checks if the selected item is perishable
        if (itemType.equals("1. Perishable")) {

            if (expiryDate.isEmpty() || storageAdvice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the expiry date and storage advice.");
                return;
            }

            try {
                java.time.LocalDate.parse(
                        expiryDate,
                        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                );
            } catch (java.time.format.DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Please enter the expiry date in DD-MM-YYYY format.");
                return;
            }

            PerishableItem perishableItem = new PerishableItem(foodName, unit, expiryDate, quantity);
            perishableItem.setStorageAdvice(storageAdvice);
            newItem = perishableItem;
        } // checks if the selected item is non-perishable
        else if (itemType.equals("2. Non-Perishable")) {

            if (category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an item category.");
                return;
            }

            NonPerishableItem nonPerishableItem = new NonPerishableItem(foodName, unit, expiryDate, quantity);
            nonPerishableItem.setCategory(category);
            newItem = nonPerishableItem;

        } else {
            JOptionPane.showMessageDialog(this, "Please select an item type.");
            return;
        }

        // adds the new pantry item to the inventory
        pantryInventory.addItem(newItem);
        //updates edit section dropdown list
        loadEditItemDropdown();

        // refreshes the inventory table and statistics
        refreshInventoryTable();
        updatePantryStatistics();

        // confirms that the item was added
        JOptionPane.showMessageDialog(this, "The item you entered has been added to the pantry successfully.");
    }//GEN-LAST:event_saveItemBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // clears the text fields
        foodNameTf.setText("");
        quantityTf.setText("");
        expiryDateTf.setText("");
        storageAdviceTf.setText("");
        categoryTf.setText("");

        // resets the dropdown menus
        unitCb.setSelectedIndex(0);
        itemTypeCb.setSelectedIndex(0);

        // disables the save button
        saveItemBtn.setEnabled(false);
    }//GEN-LAST:event_clearBtnActionPerformed

    private void updateItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateItemBtnActionPerformed
        // TODO add your handling code here:
        // gets the selected item index
        int selectedIndex = selectItemCb.getSelectedIndex() - 1;

// checks that an item is selected
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
            return;
        }

// retrieves the values from the edit form
        String foodName = foodNameTf1.getText().trim();
        String unit = unitCb1.getSelectedItem().toString();
        String expiryDate = expiryDateTf1.getText().trim();
        String itemType = itemTypeCb1.getSelectedItem().toString();
        String storageAdvice = storageAdviceTf1.getText().trim();
        String category = categoryTf1.getText().trim();

// validates the quantity
        double quantity;

        try {
            quantity = Double.parseDouble(quantityTf1.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            return;
        }

        // creates the updated item
        PantryItem updatedItem;

        if (itemType.equals("1. Perishable")) {

            if (expiryDate.isEmpty() || storageAdvice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the expiry date and storage advice.");
                return;
            }

            try {
                java.time.LocalDate.parse(
                        expiryDate,
                        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                );
            } catch (java.time.format.DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Please enter the expiry date in DD-MM-YYYY format.");
                return;
            }

            PerishableItem perishableItem = new PerishableItem(foodName, unit, expiryDate, quantity);
            perishableItem.setStorageAdvice(storageAdvice);
            updatedItem = perishableItem;

        } else if (itemType.equals("2. Non-Perishable")) {

            if (category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an item category.");
                return;
            }

            NonPerishableItem nonPerishableItem = new NonPerishableItem(foodName, unit, expiryDate, quantity);
            nonPerishableItem.setCategory(category);
            updatedItem = nonPerishableItem;

        } else {
            JOptionPane.showMessageDialog(this, "Please select an item type.");
            return;
        }

        // replaces the existing item
        pantryInventory.getItems().set(selectedIndex, updatedItem);

        // refreshes the inventory and edit dropdown
        refreshInventoryTable();
        loadEditItemDropdown();
        updatePantryStatistics();

        // confirms the update
        JOptionPane.showMessageDialog(this, "Pantry item updated successfully.");
    }//GEN-LAST:event_updateItemBtnActionPerformed

    private void clearEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearEditBtnActionPerformed

        // resets the selected pantry item
        selectItemCb.setSelectedIndex(0);

        // clears the edit form fields
        foodNameTf1.setText("");
        quantityTf1.setText("");
        expiryDateTf1.setText("");
        storageAdviceTf1.setText("");
        categoryTf1.setText("");

        // resets the dropdown selections
        itemTypeCb1.setSelectedIndex(0);
        unitCb1.setSelectedIndex(0);

        // disables the update button
        checkEditFields();
    }//GEN-LAST:event_clearEditBtnActionPerformed

    private void deleteItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemBtnActionPerformed

        // gets the selected item index
        int selectedIndex = selectItemCb.getSelectedIndex() - 1;

        // checks whether an item has been selected
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        // removes the selected item from the pantry
        pantryInventory.getItems().remove(selectedIndex);

        // refreshes the inventory and edit dropdown
        refreshInventoryTable();
        loadEditItemDropdown();
        updatePantryStatistics();

        // confirms the deletion
        JOptionPane.showMessageDialog(this, "Pantry item deleted successfully.");

    }//GEN-LAST:event_deleteItemBtnActionPerformed

    private void expiringItemsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiringItemsBtnActionPerformed

        // gets pantry items expiring within the next 2 days
        java.util.ArrayList<PantryItem> expiringItems = pantryInventory.getExpiringItems(2);

        // checks whether any items are expiring soon
        if (expiringItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no items expiring within the next 2 days.");
            return;
        }

        // creates a message containing the expiring items
        String message = "Items expiring soon:\n\n";

        for (PantryItem item : expiringItems) {
            message += item.getFoodName()
                    + " - Expires: " + item.getExpiryDate() + "\n";
        }
        // displays the expiring items
        JOptionPane.showMessageDialog(this, message);

    }//GEN-LAST:event_expiringItemsBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addItemPnl;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JLabel categoryLbl1;
    private javax.swing.JPanel categoryRowPnl;
    private javax.swing.JPanel categoryRowPnl1;
    private javax.swing.JTextField categoryTf;
    private javax.swing.JTextField categoryTf1;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton clearEditBtn;
    private javax.swing.JButton deleteItemBtn;
    private javax.swing.JPanel editButtonsPnl;
    private javax.swing.JPanel editItemPnl;
    private javax.swing.JButton expiringItemsBtn;
    private javax.swing.JPanel expiringPnl;
    private javax.swing.JLabel expiringTitleLbl;
    private javax.swing.JLabel expiringValueLbl;
    private javax.swing.JLabel expiryDateLbl;
    private javax.swing.JLabel expiryDateLbl1;
    private javax.swing.JPanel expiryDateRowPnl;
    private javax.swing.JPanel expiryDateRowPnl1;
    private javax.swing.JTextField expiryDateTf;
    private javax.swing.JTextField expiryDateTf1;
    private javax.swing.JLabel foodNameLbl;
    private javax.swing.JLabel foodNameLbl1;
    private javax.swing.JPanel foodNameRowPnl;
    private javax.swing.JPanel foodNameRowPnl1;
    private javax.swing.JTextField foodNameTf;
    private javax.swing.JTextField foodNameTf1;
    private javax.swing.JPanel inventoryPnl;
    private javax.swing.JTable inventoryTbl;
    private javax.swing.JComboBox<String> itemTypeCb;
    private javax.swing.JComboBox<String> itemTypeCb1;
    private javax.swing.JLabel itemTypeLbl;
    private javax.swing.JLabel itemTypeLbl1;
    private javax.swing.JPanel itemTypeRowPnl;
    private javax.swing.JPanel itemTypeRowPnl1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel pantryDescLbl;
    private javax.swing.JTabbedPane pantryTabbedPn;
    private javax.swing.JLabel pantryTitleLbl;
    private javax.swing.JPanel perishablePnl;
    private javax.swing.JLabel perishableTitleLbl;
    private javax.swing.JLabel perishableValueLbl;
    private javax.swing.JLabel quantityLbl;
    private javax.swing.JLabel quantityLbl1;
    private javax.swing.JPanel quantityRowPnl;
    private javax.swing.JPanel quantityRowPnl1;
    private javax.swing.JTextField quantityTf;
    private javax.swing.JTextField quantityTf1;
    private javax.swing.JButton saveItemBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTf;
    private javax.swing.JComboBox<String> selectItemCb;
    private javax.swing.JLabel selectItemLbl;
    private javax.swing.JPanel selectItemRowPnl;
    private javax.swing.JLabel storageAdviceLbl;
    private javax.swing.JLabel storageAdviceLbl1;
    private javax.swing.JPanel storageAdviceRowPnl;
    private javax.swing.JPanel storageAdviceRowPnl1;
    private javax.swing.JTextField storageAdviceTf;
    private javax.swing.JTextField storageAdviceTf1;
    private javax.swing.JPanel totalItemsPnl;
    private javax.swing.JLabel totalItemsTitleLbl;
    private javax.swing.JLabel totalItemsValueLbl;
    private javax.swing.JComboBox<String> unitCb;
    private javax.swing.JComboBox<String> unitCb1;
    private javax.swing.JLabel unitLbl;
    private javax.swing.JLabel unitLbl1;
    private javax.swing.JPanel unitRowPnl;
    private javax.swing.JPanel unitRowPnl1;
    private javax.swing.JButton updateItemBtn;
    // End of variables declaration//GEN-END:variables
}
