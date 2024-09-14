package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;

public class GuessingController {
  @FXML private Label labelTimer;
  @FXML private AnchorPane paneNoteWindow;
  @FXML private Rectangle rectCloseNotes;
  @FXML private AnchorPane paneOpenChat;
  @FXML private AnchorPane paneSuspectOne;
  @FXML private AnchorPane paneSuspectTwo;
  @FXML private AnchorPane paneSuspectThree;

  private static GameStateContext context = new GameStateContext();
  private boolean isTheif;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    // to be implemented
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleSuspectClick(MouseEvent event) throws IOException {
    AnchorPane clickedSuspect = (AnchorPane) event.getSource();
    switch (clickedSuspect.getId()) {
      case "paneSuspectOne":
        isTheif = false;
        break;
      case "paneSuspectTwo":
        isTheif = false;
        break;
      case "paneSuspectThree":
        isTheif = true;
        break;
    }
  }

  @FXML
  private void handleOpenButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(true);
  }

  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(false);
  }
}
