package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;

public class IntroDialogueSceneController {

  @FXML private AnchorPane paneDialogueBox;
  @FXML private Label labelDialogue;

  private List<String> dialogues;
  private int currentDialogueIndex = 0;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    dialogues = new ArrayList<>();
    dialogues.add("Ahhhhh! My winnings! They're gone!");
    dialogues.add(
        "I won big at the casino tonight, and I was about to leave but the bag of cash they had"
            + " given me was stolen!");
    dialogues.add("Oh! the bag of cash was found?");
    dialogues.add("But I still wanna know what kind of person would do such a thing!");
    dialogues.add("PI Masters! Please help me get to the bottom of this!");

    showNextDialogue();
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

  @FXML
  public void handleBoxClick(MouseEvent event) {
    showNextDialogue();
  }

  public void showNextDialogue() {
    if (currentDialogueIndex < dialogues.size()) {
      labelDialogue.setText(dialogues.get(currentDialogueIndex));
      currentDialogueIndex++;
    } else {
      // move onto the next scene
      try {
        App.setRoot("CrimeScene");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
