package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.classes.Suspect;
import nz.ac.auckland.se206.classes.Timer;

/**
 * The Guessing state of the game. Handles the logic for when the player is
 * making a guess about the
 * profession of the characters in the game.
 */
public class Investigating implements GameState {

  private final GameStateContext context;
  private boolean hasClueBeenInspected = false;

  private Timer timer = new Timer(60 * 5);

  private Thread updateThread = new Thread(
      () -> {
        Platform.runLater(
            () -> {
              App.getController().onTimerUpdate(this.timer.getTime().toString());
            });
      });
  private Thread timeOutThread = new Thread(
      () -> {
        Platform.runLater(
            () -> {
              handleTimeOut();
            });
      });

  /**
   * Constructs a new Guessing state with the given game state context.
   *
   * @param context the context of the game state
   */
  public Investigating(GameStateContext context) {
    this.context = context;
    timer.setExecution(updateThread);
    timer.setTimeOutThread(timeOutThread);
    System.out.println("Investigating state");
  }

  public void start() {
    timer.start();
  }

  public void sceneChange() {
    App.getController().onTimerUpdate(this.timer.getTime().toString());
    if (guessNowCheck()) {
      App.getController().unlockGuessBtn();
    }
  }

  private Boolean guessNowCheck() {
    for (Suspect currentSuspect : App.getSuspects()) {
      if (!currentSuspect.getInteracted()) {
        System.out.println("Interact will all suspects first");
        return (false);
      }
    }

    if (!hasClueBeenInspected) {
      System.out.println("Inspect the clue first");
      return (false);
    }

    return (true);
  }

  /**
   * Handles the event when a rectangle is clicked. Checks if the clicked
   * rectangle is a customer
   * and updates the game state accordingly.
   *
   * @param event       the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
  }

  @Override
  public void onGuessNow() {
    if (guessNowCheck()) {
      nextState();
    }
  }

  public void handleTimeOut() {
    System.out.println("No more Time");
    nextState();
  }

  public Timer getTimer() {
    return timer;
  }

  public void nextState() {
    timer.stop();
    context.setState(context.getGuessingState());
    try {
      App.setRoot("Guessing");
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!guessNowCheck()) {
      ((Guessing) context.getState()).handleTimeOut();
    }

  }

  public void setClueInteractionStatus() {
    hasClueBeenInspected = true;
  }
}
