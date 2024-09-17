package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Suspect;

import java.io.IOException;

public class SuspectOneController {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private TextField txtMessage;
  @FXML private Button btnSend;
  @FXML private ChoiceBox<String> choicebox;
  @FXML private MenuItem menuSuspectTwo;
  @FXML private MenuItem menuSuspectThree;

  private Suspect suspect;

  @FXML
  public void initialize() {
    this.suspect = new Suspect("Nas-ty", "Suspect", "suspect1.txt");
    // set the initial message by telling gpt to introduce itself
    suspect.getResponse(
        "Introduce yourself",
        response -> {
          labelResponse.setText(response);
        });
  }

  @FXML
  public void sendMessage(ActionEvent event) throws IOException, ApiProxyException {
    String message = txtMessage.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    txtMessage.clear();

    suspect.getResponse(
        message,
        response -> {
          labelResponse.setText(response);
        });
  }

  @FXML
  private void toSuspectTwo(ActionEvent event) {
    try {
      App.setRoot("SuspectTwo");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void toSuspectThree(ActionEvent event) {
    try {
      App.setRoot("SuspectThree");
    } catch (IOException e) {
      e.printStackTrace();
    }
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
}
