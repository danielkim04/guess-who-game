package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.*;

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
  public void initalize() {
  }

  @FXML
  public void playAgainClick() {
    App.getContext().getGameOverState().nextState();
  }

  public void handlePlayAgain() {
    App.getContext().getGameOverState().nextState();
  }

  @Override
  public void onNewChat(String chat) {
    // process gpt response and display result
    String responseLowerCase = chat.toLowerCase();
    if (responseLowerCase.contains("incorrect")) {
      setWinOrLose(false);
    } else if (responseLowerCase.contains("correct")) {
      setWinOrLose(true);
    } else {
      System.out.println("Warning! Unexpected response ... ");
    }

    // displays gpt evaluation of the user's explanation
    labelExplain.setText(chat);
  }

  public void setWinOrLose(boolean win) {
    if (win) {
      labelVictory.setVisible(true);
      labelDefeat.setVisible(false);
    } else {
      labelVictory.setVisible(false);
      labelDefeat.setVisible(true);
    }
  }

  @Override
  public void onTimerUpdate(String time) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onTimerUpdate'");
  }

  public void timeOut() {
    setWinOrLose(false);
    labelExplain.setText("Time's up! You failed to solve the case.");
  }
}
