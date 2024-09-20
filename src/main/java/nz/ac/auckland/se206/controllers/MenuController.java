package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Controller;

public class MenuController implements Controller {

  @FXML
  private Rectangle startRectangle;

  @FXML
  private Label labelStart; // Reference to StartLabel

  // Called after FXML file has been loaded
  @FXML
  public void initialize() {
    System.out.println("MenuController initialized");

    // Add any additional setup here, if needed
    if (startRectangle != null) {
      System.out.println("StartRectangle is initialized.");
    }

    if (labelStart != null) {
      System.out.println("StartLabel is initialized.");
    }
    App.setGameState(App.getContext().getGameStartedState());
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
    App.getContext().getGameStartedState().nextState();

    try {
      // Attempt to set the root scene to "room"
      App.setRoot("IntroDialogueScene");
    } catch (IOException e) {
      e.printStackTrace(); // Handle any exception that occurs during the scene change
    }
  }

  @Override
  public void onNewChat(String chat) {
    // irrelevant for this controller
  }

  @Override
  public void onTimerUpdate(String time) {
    // irrelevant for this controller
  }
}
