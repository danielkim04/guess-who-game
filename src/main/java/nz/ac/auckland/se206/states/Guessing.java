package nz.ac.auckland.se206.states;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.classes.Timer;
import nz.ac.auckland.se206.controllers.GameEndController;

public class Guessing implements GameState {
  private Timer timer = new Timer(60);
  private final GameStateContext context;
  private Thread updateThread =
      new Thread(
          () -> {
            Platform.runLater(
                () -> {
                  App.getController().onTimerUpdate(this.timer.getTime().toString());
                });
          });
  private Thread timeOutThread =
      new Thread(
          () -> {
            Platform.runLater(
                () -> {
                  handleTimeOut();
                });
          });

  public Guessing(GameStateContext context) {
    this.context = context;
    timer.setExecution(updateThread);
    timer.setTimeOutThread(timeOutThread);
    System.out.println("start guessing");
  }

  public void start() {
    timer.start();
  }

  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleRectangleClick'");
  }

  @Override
  public void handleGuessClick() throws IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleGuessClick'");
  }

  public void handleTimeOut() {
    // called when time runs out
    System.out.println("No more Guessing Time");
    nextState();
    if (App.getController() instanceof GameEndController) {
      GameEndController controller = (GameEndController) App.getController();
      controller.timeOut();
    } else {
      System.out.println("Error: Controller is not GameEndController");
    }
  }

  public void nextState() {
    context.setState(context.getGameOverState());
    try {
      App.setRoot("GameEnd");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Timer getTimer() {
    return timer;
  }
}
