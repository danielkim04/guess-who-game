package nz.ac.auckland.se206;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import org.yaml.snakeyaml.Yaml;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.Guessing;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Investigating;

/**
 * Context class for managing the state of the game. Handles transitions between different game
 * states and maintains game data such as the professions and rectangle IDs.
 */
public class GameStateContext {

  private final String rectIdToGuess;
  private final String professionToGuess;
  private final Map<String, String> rectanglesToProfession;
  private final GameStarted gameStartedState;
  private final Investigating investigatingState;
  private final Guessing guessingState;
  private final GameOver gameOverState;
  private GameState gameState;

  /** Constructs a new GameStateContext and initializes the game states and professions. */
  public GameStateContext() {
    gameStartedState = new GameStarted(this);
    investigatingState = new Investigating(this);
    gameOverState = new GameOver(this);
    guessingState = new Guessing(this);

    gameState = gameStartedState; // Initial state
    Map<String, Object> obj = null;
    Yaml yaml = new Yaml();
    try (InputStream inputStream =
        GameStateContext.class.getClassLoader().getResourceAsStream("data/professions.yaml")) {
      if (inputStream == null) {
        throw new IllegalStateException("File not found!");
      }
      obj = yaml.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }

    @SuppressWarnings("unchecked")
    List<String> professions = (List<String>) obj.get("professions");

    Random random = new Random();
    Set<String> randomProfessions = new HashSet<>();
    while (randomProfessions.size() < 3) {
      String profession = professions.get(random.nextInt(professions.size()));
      randomProfessions.add(profession);
    }

    String[] randomProfessionsArray = randomProfessions.toArray(new String[3]);
    rectanglesToProfession = new HashMap<>();
    rectanglesToProfession.put("rectPerson1", randomProfessionsArray[0]);
    rectanglesToProfession.put("rectPerson2", randomProfessionsArray[1]);
    rectanglesToProfession.put("rectPerson3", randomProfessionsArray[2]);

    int randomNumber = random.nextInt(3);
    rectIdToGuess =
        randomNumber == 0 ? "rectPerson1" : ((randomNumber == 1) ? "rectPerson2" : "rectPerson3");
    professionToGuess = rectanglesToProfession.get(rectIdToGuess);
  }

  /**
   * This method sets the current state of the game.
   *
   * @param state the new state to set
   */
  public void setState(GameState state) {
    this.gameState = state;
    this.gameState.start();
  }

  /**
   * This method gets the current state of the game.
   *
   * @return the current state of the game
   */
  public GameState getState() {
    return (this.gameState);
  }

  /**
   * This method gets the initial game started state.
   *
   * @return the game started state
   */
  public GameState getGameStartedState() {
    return gameStartedState;
  }

  /**
   * This method gets the investigating state.
   *
   * @return the investigating state
   */
  public GameState getInvestigatingState() {
    return investigatingState;
  }

  /**
   * This method gets the guessing state.
   *
   * @return the guessing state
   */
  public GameState getGuessingState() {
    return guessingState;
  }

  /**
   * This method gets the game over state.
   *
   * @return the game over state
   */
  public GameState getGameOverState() {
    return gameOverState;
  }

  /**
   * This method gets the profession to be guessed.
   *
   * @return the profession to guess
   */
  public String getProfessionToGuess() {
    return professionToGuess;
  }

  /**
   * This method gets the ID of the rectangle to be guessed.
   *
   * @return the rectangle ID to guess
   */
  public String getRectIdToGuess() {
    return rectIdToGuess;
  }

  /**
   * This method gets the profession associated with a specific rectangle ID.
   *
   * @param rectangleId the rectangle ID
   * @return the profession associated with the rectangle ID
   */
  public String getProfession(String rectangleId) {
    return rectanglesToProfession.get(rectangleId);
  }

  /**
   * This method handles the event when a rectangle is clicked.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    gameState.handleRectangleClick(event, rectangleId);
  }

  public void handleAnchorClick(MouseEvent event, String anchorId) {}

  /**
   * This method handles the event when the guess button is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  public void onGuessNow() throws IOException {
    gameState.onGuessNow();
  }
}
