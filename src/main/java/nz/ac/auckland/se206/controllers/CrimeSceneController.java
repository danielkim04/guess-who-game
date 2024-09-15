package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.classes.*;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class CrimeSceneController implements Controller {

  @FXML private Rectangle rectClueBag;
  @FXML private Rectangle rectClueBook;
  @FXML private Rectangle rectClueNote;
  @FXML private Label labelTimer;
  @FXML private AnchorPane paneNoteWindow;
  @FXML private Rectangle rectCloseNotes;
  @FXML private AnchorPane paneOpenChat;
  @FXML private Pane paneBase;
  @FXML private ImageView imgMap;
  @FXML private Label timerLabel;

  private static GameStateContext context = new GameStateContext();

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    // to be implemented
    System.out.println("Entering Crime Scene");
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
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    // to be implemented
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }

  @FXML
  private void handleOpenButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box opened");
    paneNoteWindow.setVisible(true);
  }

  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box Closed");
    paneNoteWindow.setVisible(false);
  }

  @FXML
  private void handleMapClick(MouseEvent event) throws IOException {
    // to be implemented
  }

  @Override
  public void onNewChat(String chat) {
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  @Override
  public void onTimerUpdate(String time) {
    timerLabel.setText(time);
  }
}
