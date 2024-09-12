package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class MenuController {

    @FXML
    private Rectangle StartRectangle;

    // Called after FXML file has been loaded
    @FXML
    public void initialize() {
        System.out.println("MenuController initialized");

        // Add any additional setup here, if needed
        if (StartRectangle != null) {
            System.out.println("StartRectangle is initialized.");
        }
    }

    @FXML
    private void onStartRectangleClick(MouseEvent event) {
        System.out.println("StartRectangle pressed!");
    }
}
