package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class GuessingController {
  @FXML private Label labelTimer;
  @FXML private AnchorPane paneNoteWindow;
  @FXML private Rectangle rectCloseNotes;
  @FXML private AnchorPane paneOpenChat;
  @FXML private AnchorPane paneSuspectOne;
  @FXML private AnchorPane paneSuspectTwo;
  @FXML private AnchorPane paneSuspectThree;
  @FXML private TextArea txtaExplanation;
  @FXML private Button btnSend;
  @FXML private AnchorPane paneExplanation;
  @FXML private Label labelTitle;
  @FXML private ImageView imgChosenSuspect;

  private boolean isThief;

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
    String filename = "";
    AnchorPane clickedSuspect = (AnchorPane) event.getSource();
    switch (clickedSuspect.getId()) {
      case "paneSuspectOne":
        isThief = false;
        filename = "man 1.png"; // modify filename
        break;
      case "paneSuspectTwo":
        isThief = false;
        filename = "man 1.png"; // modify filename
        break;
      case "paneSuspectThree":
        isThief = true;
        filename = "man 1.png"; // modify filename
        break;
    }
    transitionToExplanation(getClass().getResource("/fxml/source/" + filename));
  }

  private void transitionToExplanation(URL url) {
    paneExplanation.setVisible(true);
    labelTitle.setText("Present Evidence");
    Image image = new Image(url.toExternalForm());
    imgChosenSuspect.setImage(image);
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
