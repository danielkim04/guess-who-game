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
  private Label labelResult;
  @FXML private Label labelExplain;
  @FXML private ImageView imageCriminal;
  @FXML private Button buttonPlayAgain;

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

    // displays gpt evaluation of the user's explanation
    labelExplain.setText(chat);

  }

  @Override
  public void onTimerUpdate(String time) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onTimerUpdate'");
  }

}
