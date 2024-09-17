package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.Suspect;

import java.io.IOException;

public class SuspectTwoController implements Controller {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private TextField txtMessage;
  @FXML private Button btnSend;
  @FXML private MenuItem menuSuspectOne;
  @FXML private MenuItem menuSuspectThree;
  @FXML private MenuItem menuCrimeScene;

  private Suspect suspect;

  @FXML
  public void initialize() {
    this.suspect = new Suspect("V", "Suspect", "suspect2.txt");
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
  private void toSuspectOne(ActionEvent event) {
    try {
      App.setRoot("SuspectOne");
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

  @FXML
  private void toCrimeScene(ActionEvent event) {
    try {
      App.setRoot("CrimeScene");
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

  @Override
  public void onNewChat(String chat) {

  }

  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }
}
