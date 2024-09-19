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
import nz.ac.auckland.se206.classes.*;

public class IntroDialogueSceneController implements Controller {

  @FXML
  private AnchorPane paneDialogueBox;
  @FXML
  private Label labelDialogue;
  @FXML
  private Label labelTimer;


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
        "I had just won big at the casino, a life-changing amount. But when I arrived at my hotel,"
            + " most of the money was gone.");
    dialogues.add("I'm glad you're here I'm counting on you to solve this detective.");
    dialogues.add("I'll be in the lobby if you need me.");

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

  @Override
  public void onNewChat(String chat) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }
}
