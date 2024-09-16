package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Suspect;

import java.io.IOException;

public class SuspectTwoController {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private TextField txtMessage;
  @FXML private Button btnSend;
  @FXML private ChoiceBox<String> choicebox;

  private Suspect suspect;

  @FXML
  public void initialize() {
    // initialise dropdown menu
    choicebox.getItems().addAll("Suspect 1", "Suspect 3");
    // handle scene transition based on dropdown selection
    choicebox.setOnAction(
        event -> {
          String selected = choicebox.getValue();
          if (selected.equals("Suspect 1")) {
            try {
              App.setRoot("SuspectOne");
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (selected.equals("Suspect 3")) {
            try {
              App.setRoot("SuspectThree");
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

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
