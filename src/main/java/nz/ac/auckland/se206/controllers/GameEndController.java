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
  private Label labelExplain;
  private ImageView imageCriminal;
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  @Override
  public void onTimerUpdate(String time) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onTimerUpdate'");
  }

}
