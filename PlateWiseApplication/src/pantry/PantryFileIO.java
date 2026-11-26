/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Merlin M
 */
public class PantryFileIO {

    // saves the pantry inventory to a file
    public static void savePantry(Pantry pantry, String fileName) {

        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream output = new ObjectOutputStream(file);

            output.writeObject(pantry);

            output.close();
            file.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error saving pantry: " + e.getMessage());
        }
    }

    // loads the pantry inventory from a file
    public static Pantry loadPantry(String fileName) {

        File file = new File(fileName);

        // checks if a saved pantry file exists
        if (!file.exists()) {
            return new Pantry();
        }

        try {
            FileInputStream inputFile = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(inputFile);

            Pantry pantry = (Pantry) input.readObject();

            input.close();
            inputFile.close();

            return pantry;

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading pantry: " + e.getMessage());

            return new Pantry();
        }
    }

}
