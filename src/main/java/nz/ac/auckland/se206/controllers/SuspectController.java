package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.CharacterInteractionManager;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.Suspect;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Investigating;

/** This is the Controller class for the Suspect scenes. */
public class SuspectController implements Controller {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private Label char1;
  @FXML private Label char2;
  @FXML private Label char3;
  @FXML private Label char4; // Interactable 0/1
  @FXML private TextArea txtMessage;
  @FXML private Button btnSend;
  @FXML private MenuItem menuLobby;
  @FXML private MenuItem menuBar;
  @FXML private MenuItem menuTables;
  @FXML private MenuItem menuCrimeScene;
  @FXML private Button btnGuessNow;
  @FXML private Rectangle rectSendButton;
  @FXML private ImageView imgSuspect;
  @FXML private ImageView imgSuspectGif;
  @FXML private AnchorPane lobbyButtonAnchorPane;
  @FXML private AnchorPane crimeSceneButtonAnchorPane;
  @FXML private AnchorPane barButtonAnchorPane;
  @FXML private AnchorPane tablesButtonAnchorPane;
  @FXML private AnchorPane mapMenuAnchorPane;
  @FXML private Rectangle rectDisableButton;
  @FXML private ImageView imgGuessButton;
  @FXML private ImageView imgButtonNoColor;
  @FXML private Label markObjectiveLabel;
  @FXML private Label susanObjectiveLabel;
  @FXML private Label anthonyObjectiveLabel;
  @FXML private Label suspectObjectiveLabel;
  @FXML private Label clueObjectiveLabel;
  @FXML private Label allClueObjectiveLabel;

  private Suspect suspect;
  private Timeline timeline;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @FXML
  public void initialize() {
    this.suspect = App.getCurrentSuspect();
    createMap();
    displayTextSlowly(". . .");
    // set the initial message by telling gpt to introduce itself
    suspect.getResponse(
        "Introduce yourself",
        response -> {
          timeline.stop();
          labelResponse.setText(response);
          // add function call to loadGif here if gif works
        });
    onSceneOpened();
  }

  /** This method is responsible for creating the map. */
  private void createMap() {
    App.addToLocationMap(lobbyButtonAnchorPane, "SuspectOne");
    App.addToLocationMap(tablesButtonAnchorPane, "SuspectTwo");
    App.addToLocationMap(barButtonAnchorPane, "SuspectThree");
    App.addToLocationMap(crimeSceneButtonAnchorPane, "CrimeScene");
  }

  /** Method to update the labels based on the interaction status of the characters. */
  public void onSceneOpened() {
    // Get the instance of the singleton to check the character states
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    // Update the labels based on the interaction status of the characters
    updateLabels(manager);
  }

  /**
   * Method to update the labels based on the interaction status of the characters.
   *
   * @param manager the character interaction manager
   */
  private void updateLabels(CharacterInteractionManager manager) {
    // Update char1 (Mark)
    int numSuspects = 0;
    if (manager.isTalkedToCharacter1()) {
      numSuspects++;
      markObjectiveLabel
          .getStylesheets()
          .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
    }
    // Update char2 (Anthony)
    if (manager.isTalkedToCharacter2()) {
      numSuspects++;
      anthonyObjectiveLabel
          .getStylesheets()
          .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
    }
    // Update char3 (Susan)
    if (manager.isTalkedToCharacter3()) {
      numSuspects++;
      susanObjectiveLabel
          .getStylesheets()
          .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
    }

    suspectObjectiveLabel.setText(" - Speak to Suspects " + numSuspects + "/3");
    if (numSuspects >= 3) {
      suspectObjectiveLabel
          .getStylesheets()
          .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
    }
    // Update char4 (Interactable)
    int numClues = 0;
    if (manager.isInteractableClicked1()) {
      numClues++;
    }
    if (manager.isInteractableClicked2()) {
      numClues++;
    }
    if (manager.isInteractableClicked3()) {
      numClues++;
    }
    if (numClues > 0) {
      clueObjectiveLabel
          .getStylesheets()
          .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
      allClueObjectiveLabel.setText("- (optional) Find All Clues " + numClues + "/3");
      if (numClues >= 3) {
        allClueObjectiveLabel
            .getStylesheets()
            .add(App.class.getResource("/css/Strikethrough.css").toExternalForm());
      }
    }
  }

  /** Method to handle the interaction with character 1 (Mark). */
  public void onCharacter1Interaction() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    manager.setTalkedToCharacter1(true);
    System.out.println("Talked to Character 1!");
    onSceneOpened();
  }

  /** This method is responsible for handling interaction with Character 2. */
  public void onCharacter2Interaction() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    manager.setTalkedToCharacter2(true);
    System.out.println("Talked to Character 2!");
    onSceneOpened();
  }

  /** This method is responsible for handling interaction with Character 3. */
  public void onCharacter3Interaction() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    manager.setTalkedToCharacter3(true);
    System.out.println("Talked to Character 3!");
    onSceneOpened();
  }

  /** This method is used to check if all characters have been interacted with. */
  public void checkAllInteractions() {
    // Get the instance of the singleton to check the character states
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    // Check if all characters have been interacted with
    if (manager.isTalkedToCharacter1()
        && manager.isTalkedToCharacter2()
        && manager.isTalkedToCharacter3()) {
      System.out.println("All characters have been interacted with.");
    } else {
      System.out.println("Some characters have not been interacted with yet.");
    }
  }

  /** Check if the interactable object has been clicked. */
  public void checkInteractable() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    if (manager.isInteractableClicked1()) {
      System.out.println("The interactable object has been clicked.");
    } else {
      System.out.println("The interactable object has not been clicked yet.");
    }
  }

  /** This method handles the send message button click event. */
  @FXML
  private void onSendMessage() {
    // get the message from the text area
    String message = txtMessage.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    // update suspect engagement status
    this.suspect.interacted();
    if (this.suspect.getName().equals("Mark")) {
      onCharacter1Interaction();
    } else if (this.suspect.getName().equals("Anthony")) {
      onCharacter2Interaction();
    } else {
      onCharacter3Interaction();
    }
    GameState curGameState = App.getContext().getState();
    if (curGameState instanceof Investigating) {
      ((Investigating) curGameState).sceneChange();
    } else {
      System.out.println("Not investigating");
    }
    displayTextSlowly(". . .");
    txtMessage.clear();

    // get the response from the suspect
    suspect.getResponse(
        message,
        response -> {
          // display the response
          timeline.stop();
          labelResponse.setText(response);
        });
  }

  /**
   * This method handles the map button click event.
   *
   * @param event the mouse event
   */
  @FXML
  private void onChangeArea(MouseEvent event) {
    handleCloseMap();
    if (event.getSource() instanceof AnchorPane) {
      App.changeSceneMap((AnchorPane) event.getSource());
    }
  }

  /**
   * This method handles the map button click event.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleMapClick(MouseEvent event) {
    Boolean menuStatus = mapMenuAnchorPane.isVisible();
    mapMenuAnchorPane.setDisable(menuStatus);
    mapMenuAnchorPane.setVisible(!menuStatus);
  }

  /** This method handles the close map button click event. */
  @FXML
  private void handleCloseMap() {
    mapMenuAnchorPane.setDisable(true);
    mapMenuAnchorPane.setVisible(false);
  }

  /**
   * This method handles the guess now button click event.
   *
   * @param event the mouse event
   */
  @FXML
  private void onGuessNow(MouseEvent event) {
    try {
      App.getContext().getState().onGuessNow();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is used to display the text slowly.
   *
   * @param text the text to display
   */
  private void displayTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    labelResponse.setText(""); // Clear the label initially
    displayedText.append("Loading");

    timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.millis(500 * index), // Delay each letter by 100ms
              e -> {
                displayedText.append(text.charAt(index)); // Append the current letter
                labelResponse.setText(
                    displayedText.toString()); // Update the label with the new text
              });
      timeline.getKeyFrames().add(keyFrame);
    }

    // Start the timeline animation
    timeline.play();
  }

  /** This method is used to load the gif of the suspect. */
  public void loadGif() {
    Image gif = null;
    switch (suspect.getName()) {
      case "Mark":
        gif = new Image(getClass().getResource("/gif/lobbyNoWater.gif").toExternalForm());
        break;
      case "Susan":
        gif = new Image(getClass().getResource("/gif/barNoWater2.gif").toExternalForm());
        break;
      case "Anthony":
        gif = new Image(getClass().getResource("/gif/casinoNoWater.gif").toExternalForm());
        break;
    }
    Image finalGif = gif;
    Task<Void> loadGifTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Update the ImageView on the JavaFX Application Thread
            Platform.runLater(
                () -> {
                  imgSuspectGif.setImage(finalGif);
                });

            return null;
          }
        };

    // Run the task in a background thread
    new Thread(loadGifTask).start();
  }

  /**
   * This method handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {}

  /**
   * This method handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    if (event.getCode().equals(KeyCode.ENTER)) {
      onSendMessage();
    }
  }

  @Override
  public void onNewChat(String chat) {}

  /**
   * This method handles the timer update event.
   *
   * @param time the current time
   */
  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }

  /** this method locks the guess button. */
  @Override
  public void unlockGuessBtn() {
    // btnGuessNow.setDisable(false);
    imgGuessButton.setVisible(true);
    imgButtonNoColor.setVisible(false);
    rectDisableButton.setVisible(false);
  }
}
