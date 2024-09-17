package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.Suspect;

import java.io.IOException;

public class SuspectThreeController implements Controller {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private TextField txtMessage;
  @FXML private Button btnSend;
  @FXML private MenuItem menuSuspectTwo;
  @FXML private MenuItem menuSuspectOne;
  @FXML private MenuItem menuCrimeScene;
  private Suspect suspect;
  private Timeline timeline;

  @FXML
  public void initialize() {
    this.suspect = new Suspect("D", "Suspect", "suspect3.txt");
    displayTextSlowly(". . .");
    // set the initial message by telling gpt to introduce itself
    suspect.getResponse(
        "Introduce yourself",
        response -> {
          timeline.stop();
          labelResponse.setText(response);
        });
  }

  @FXML
  public void sendMessage(ActionEvent event) throws IOException, ApiProxyException {
    String message = txtMessage.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    displayTextSlowly(". . .");
    txtMessage.clear();

    suspect.getResponse(
        message,
        response -> {
          timeline.stop();
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
  private void toSuspectOne(ActionEvent event) {
    try {
      App.setRoot("SuspectOne");
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

  private void displayTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    labelResponse.setText(""); // Clear the label initially
    displayedText.append("Loading");

    timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame =
              new KeyFrame(
                      Duration.millis(500 * index), // Delay each letter by 100ms
                      e -> {
                        displayedText.append(text.charAt(index)); // Append the current letter
                        labelResponse.setText(displayedText.toString()); // Update the label with the new text
                      });
      timeline.getKeyFrames().add(keyFrame);
    }

    // Start the timeline animation
    timeline.play();
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
