package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.classes.Timer;
import nz.ac.auckland.se206.controllers.GameEndController;
import nz.ac.auckland.se206.controllers.GuessingController;

/**
 * The Guessing state of the game. Handles the interactions when the player is making a guess,
 * allowing the player to enter an explanation for their guess.
 */
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

  /** Constructs a new Guessing state with the given game state context. */
  public Guessing(GameStateContext context) {
    this.context = context;
    timer.setExecution(updateThread);
    timer.setTimeOutThread(timeOutThread);
    System.out.println("start guessing");
  }

  public void start() {
    timer.start();
  }

  /**
   * Handles the event when a rectangle is clicked. Informs the player that the game is over and
   * provides the profession of the clicked character if applicable.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleRectangleClick'");
  }

  /**
   * Handles the event when the guess button is clicked. Informs the player that the game is over
   * and no further guesses can be made.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void onGuessNow() throws IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onGuessNow'");
  }

  /** Handles the time out event. */
  public void handleTimeOut() {
    // if the time runs out but the player has entered some text, automatically send
    // explanation
    timer.stop();
    System.out.println("No more Guessing Time");
    GuessingController guessingController = (GuessingController) App.getController();
    if (!guessingController.getPlayerExplanation().isEmpty()) {
      guessingController.sendExplanationTimeOut();
      return;
    }

    // if the player has not entered any text, go to game over scene
    nextState();
    if (App.getController() instanceof GameEndController) {
      GameEndController controller = (GameEndController) App.getController();
      controller.timeOut();
    } else {
      System.out.println("Error: Controller is not GameEndController");
    }
  }

  /** Changes the state to GameOverState. */
  public void nextState() {
    context.setState(context.getGameOverState());
    try {
      App.setRoot("GameEnd");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the timer.
   *
   * @return the timer
   */
  public Timer getTimer() {
    return timer;
  }
}
