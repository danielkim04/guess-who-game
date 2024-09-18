package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.classes.*;

public class GameEndController implements Controller {

  @FXML
  private Label labelResult;
  private Label labelExplain;
  private ImageView imageCriminal;

  @FXML
  public void initalize() {

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
