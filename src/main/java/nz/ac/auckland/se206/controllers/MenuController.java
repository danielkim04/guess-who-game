package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class MenuController {

  @FXML private Rectangle StartRectangle;

  @FXML private Label StartLabel; // Reference to StartLabel

  // Called after FXML file has been loaded
  @FXML
  public void initialize() {
    System.out.println("MenuController initialized");

    // Add any additional setup here, if needed
    if (StartRectangle != null) {
      System.out.println("StartRectangle is initialized.");
    }

    if (StartLabel != null) {
      System.out.println("StartLabel is initialized.");
    }
  }

  @FXML
  private void onStartRectangleClick(MouseEvent event) {
    System.out.println("StartRectangle pressed!");
    startGame();
  }

  @FXML
  private void onStartLabelClick(MouseEvent event) {
    System.out.println("StartLabel pressed!");
    startGame();
  }

  private void startGame() {
    System.out.println("Game Starting");
    try {
      // Attempt to set the root scene to "room"
      App.setRoot("IntroDialogueScene");
    } catch (IOException e) {
      e.printStackTrace(); // Handle any exception that occurs during the scene change
    }
  }
}
