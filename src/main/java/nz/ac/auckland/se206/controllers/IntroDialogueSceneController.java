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

  @FXML
  private AnchorPane paneDialogueBox;
  @FXML
  private Label labelDialogue;

  private List<String> dialogues;
  private int currentDialogueIndex = 0;

  /**
   * Initializes the room view. If it's the first time initialization, it will
   * provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    dialogues = new ArrayList<>();
    dialogues.add("NO! My winnings! They're gone!");
    dialogues.add(
        "I had just won big at the casino, a life-changing amount. But the moment I turned my back,"
            + " the bag of cash disappeared.");
    dialogues.add("You say they found the bag? that's ... something.");
    dialogues.add("But you're still looking into me, aren't you? I can see it in your eyes.");
    dialogues.add("Come on, I need you to find the real culprit. I had nothing to do with this.");

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
