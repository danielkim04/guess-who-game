package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.classes.*;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The Guessing state of the game. Handles the logic for when the player is making a guess about the
 * profession of the characters in the game.
 */
public class Investigating implements GameState {

  private final GameStateContext context;
  private boolean talkedToSuspectOne = false;
  private boolean talkedToSuspectTwo = false;
  private boolean talkedToSuspectThree = false;

  private Timer timer = new Timer(60);

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

  /**
   * Handles the event when a rectangle is clicked. Checks if the clicked rectangle is a customer
   * and updates the game state accordingly.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {}

  @Override
  public void handleGuessClick() {
    if (!talkedToSuspectOne || !talkedToSuspectTwo || !talkedToSuspectThree) {
      // tell user to talk to all suspects
      System.out.println("Talk to all suspects first");
      return;
    }
    timer.stop();
    context.setState(context.getGuessingState());
    try {
      App.setRoot("Guessing");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void handleTimeOut() {
    System.out.println("No more Time");

    try {
      App.setRoot("Guessing");
    } catch (IOException e) {
      e.printStackTrace();
    }
    context.setState(context.getGuessingState());
  }

  public void setSuspectState(String suspect) {
    switch (suspect) {
      case "SuspectOne":
        talkedToSuspectOne = true;
        break;
      case "SuspectTwo":
        talkedToSuspectTwo = true;
        break;
      case "SuspectThree":
        talkedToSuspectThree = true;
        break;
      default:
        break;
    }
  }

  public boolean getTalkedToSuspectOne() {
    return talkedToSuspectOne;
  }

  public boolean getTalkedToSuspectTwo() {
    return talkedToSuspectTwo;
  }

  public boolean getTalkedToSuspectThree() {
    return talkedToSuspectThree;
  }
}
