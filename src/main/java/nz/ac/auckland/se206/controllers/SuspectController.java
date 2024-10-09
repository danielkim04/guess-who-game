package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.Suspect;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Investigating;

public class SuspectController implements Controller {
  @FXML private Label labelTimer;
  @FXML private Label labelResponse;
  @FXML private TextArea txtMessage;
  @FXML private Button btnSend;
  @FXML private MenuItem menuLobby;
  @FXML private MenuItem menuBar;
  @FXML private MenuItem menuTables;
  @FXML private MenuItem menuCrimeScene;
  @FXML private Button btnGuessNow;
  @FXML private Rectangle rectSendButton;
  @FXML private MenuButton menuButtonMap;
  @FXML private ImageView imgSuspect;
  @FXML private ImageView imgSuspectGif;
  @FXML private AnchorPane lobbyButtonAnchorPane;
  @FXML private AnchorPane crimeSceneButtonAnchorPane;
  @FXML private AnchorPane barButtonAnchorPane;
  @FXML private AnchorPane tablesButtonAnchorPane;
  @FXML private AnchorPane mapMenuAnchorPane;

  private Suspect suspect;
  private Timeline timeline;

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
          loadGif();
        });
  }

  private void createMap() {
    App.addToLocationMap(lobbyButtonAnchorPane, "SuspectOne");
    App.addToLocationMap(tablesButtonAnchorPane, "SuspectTwo");
    App.addToLocationMap(barButtonAnchorPane, "SuspectThree");
    App.addToLocationMap(crimeSceneButtonAnchorPane, "CrimeScene");
  }

  @FXML
  private void onSendMessage() {
    String message = txtMessage.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    // update suspect engagement status
    this.suspect.interacted();
    GameState curGameState = App.getContext().getState();
    if (curGameState instanceof Investigating) {
      ((Investigating) curGameState).sceneChange();
    } else {
      System.out.println("Not investigating");
    }
    displayTextSlowly(". . .");
    txtMessage.clear();

    suspect.getResponse(
        message,
        response -> {
          timeline.stop();
          labelResponse.setText(response);
        });
  }

  @FXML
  private void onChangeArea(MouseEvent event) {
    handleCloseMap();
    if (event.getSource() instanceof AnchorPane) {
      App.changeSceneMap((AnchorPane) event.getSource());
    }
  }

  @FXML
  private void handleMapClick(MouseEvent event) {
    Boolean menuStatus = mapMenuAnchorPane.isVisible();
    mapMenuAnchorPane.setDisable(menuStatus);
    mapMenuAnchorPane.setVisible(!menuStatus);
  }

  @FXML
  private void handleCloseMap() {
    mapMenuAnchorPane.setDisable(true);
    mapMenuAnchorPane.setVisible(false);
  }

  @FXML
  private void onGuessNow(ActionEvent event) {
    try {
      App.getContext().getState().onGuessNow();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {}

  /**
   * Handles the key released event.
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

  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }

  @Override
  public void unlockGuessBtn() {
    btnGuessNow.setDisable(false);
  }
}
