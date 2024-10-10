package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller for the GameEnd scene
 */
public class GameEndController implements Controller {

  @FXML
  private Label labelVictory;
  @FXML
  private Label labelDefeat;
  @FXML
  private Label labelExplain;
  @FXML
  private ImageView imageCriminal;
  @FXML
  private Button buttonPlayAgain;
  @FXML
  private ImageView imgLoadingWheel;

  @FXML
  public void initialize() {
    // Initialization code if necessary
  }

  @FXML
  public void playAgainClick() {
    App.getContext().getGameOverState().nextState();
  }

  @FXML
  private void onPlayAgain() {
    App.getContext().getGameOverState().nextState();
  }

  @Override
  public void onNewChat(String chat) {
    // process GPT response and display result
    showLoadingWheel(false);
    String responseLowerCase = chat.toLowerCase();
    if (responseLowerCase.contains("incorrect")) {
      setWinOrLose(false);
    } else if (responseLowerCase.contains("correct")) {
      setWinOrLose(true);
    } else {
      System.out.println("Warning! Unexpected response ... ");
    }

    // displays GPT evaluation of the user's explanation
    labelExplain.setText(chat);
  }

  /**
   * Displays the explanation for the wrong guess
   *
   * @param suspectName the name of the suspect that was guessed
   */
  public void onWrongGuess(String suspectName) {
    // displays the explanation for the wrong guess
    String explanation = suspectName + " is not the thief.\n"; // first line
    try {
      // get text from file
      URL resourceUrl = PromptEngineering.class.getClassLoader().getResource("prompts/wrongGuess.txt");
      String textToAppend = new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
      explanation += textToAppend;
    } catch (Exception e) {
      e.printStackTrace();
    }
    labelExplain.setText(explanation);
    setWinOrLose(false);
  }

  /**
   * Sets the win or lose message
   *
   * @param win true if the player won, false if the player lost
   */
  public void setWinOrLose(boolean win) {
    if (win) {
      labelVictory.setVisible(true);
      labelDefeat.setVisible(false);
    } else {
      labelVictory.setVisible(false);
      labelDefeat.setVisible(true);
    }
  }

  public void showLoadingWheel(Boolean show) {
    imgLoadingWheel.setVisible(show);
  }

  @Override
  public void onTimerUpdate(String time) {
    System.out.println("Ignoring timer");
  }

  public void timeOut() {
    setWinOrLose(false);
    labelExplain.setText("Time's up! You failed to solve the case.");
  }

  @Override
  public void unlockGuessBtn() {
  }
}
