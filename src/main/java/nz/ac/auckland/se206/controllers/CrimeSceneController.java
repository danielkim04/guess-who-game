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

public class CrimeSceneController {

  @FXML private Rectangle rectClueBag;
  @FXML private Rectangle rectClueBook;
  @FXML private Rectangle rectClueNote;
  @FXML private Label labelTimer;
  @FXML private AnchorPane paneNoteWindow;
  @FXML private Rectangle rectCloseNotes;
  @FXML private AnchorPane paneOpenChat;
  @FXML private Pane paneBase;
  @FXML private ImageView imgMap;
  @FXML private AnchorPane bagInteractPane; // Declare your pane here

  private static GameStateContext context = new GameStateContext();

  @FXML
  public void initialize() {
    // Hide bagInteractPane initially
    bagInteractPane.setVisible(false);
  }

  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    // to be implemented
  }

  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }

  @FXML
  private void handleOpenButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(true);
  }

  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(false);
  }

  @FXML
  private void handleMapClick(MouseEvent event) throws IOException {
    // to be implemented
  }

  // New method to handle clicks on rectClueBag
  @FXML
  private void handleClueBagClick(MouseEvent event) {
    // Show the bagInteractPane when rectClueBag is clicked
    bagInteractPane.setVisible(true);
  }
}
