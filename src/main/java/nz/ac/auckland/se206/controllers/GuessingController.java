package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.NotesSyncManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.states.Guessing;

public class GuessingController implements Controller {
  @FXML
  private Label labelTimer;
  @FXML
  private AnchorPane paneNoteWindow;
  @FXML
  private Rectangle rectCloseNotes;
  @FXML
  private AnchorPane paneOpenChat;
  @FXML
  private AnchorPane paneSuspectOne;
  @FXML
  private AnchorPane paneSuspectTwo;
  @FXML
  private AnchorPane paneSuspectThree;
  @FXML
  private TextArea txtaExplanation;
  @FXML
  private TextArea guessingNotes;
  @FXML
  private Button btnSend;
  @FXML
  private AnchorPane paneExplanation;
  @FXML
  private Label labelTitle;
  @FXML
  private ImageView imgChosenSuspect;

  private String suspectName;
  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() {
    // Load the synced notes from NotesSyncManager when the scene is initialized
    guessingNotes.setText(NotesSyncManager.getNotesText());
    refreshNotes();
  }

  @FXML
  private void onGuessingNotesChanged() {
    // Update the notes in NotesSyncManager when the user edits the TextArea
    NotesSyncManager.setNotesText(guessingNotes.getText());
  }

  // Call this method when you want to manually refresh the notes
  public void refreshNotes() {
    guessingNotes.setText(NotesSyncManager.getNotesText());
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleSuspectClick(MouseEvent event) throws IOException {
    String filename = "";
    AnchorPane clickedSuspect = (AnchorPane) event.getSource();
    switch (clickedSuspect.getId()) {
      case "paneSuspectOne":
        suspectName = "Mark";
        filename = "Suspect_1_Cropped.png"; // modify filename
        break;
      case "paneSuspectTwo":
        suspectName = "Susan";
        filename = "Suspect_3_Cropped.png"; // modify filename
        break;
      case "paneSuspectThree":
        suspectName = "Anthony";
        filename = "Suspect_2_Cropped.png"; // modify filename
        break;
    }
    transitionToExplanation(getClass().getResource("/images/" + filename));
  }

  private void transitionToExplanation(URL url) {
    paneExplanation.setVisible(true);
    labelTitle.setText("Present Evidence");
    Image image = new Image(url.toExternalForm());
    imgChosenSuspect.setImage(image);
  }

  private String loadSystemPrompt() throws IOException, URISyntaxException {
    Map<String, String> map = new HashMap<>();
    map.put("suspect", suspectName);
    return PromptEngineering.getPrompt("chat.txt", map);
  }

  public void initialiseChat() {
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest = new ChatCompletionRequest(config)
          .setN(1)
          .setTemperature(0.1)
          .setTopP(0.9)
          .setMaxTokens(100);
      chatCompletionRequest.addMessage(new ChatMessage("system", loadSystemPrompt()));
    } catch (ApiProxyException | URISyntaxException | IOException e) { // ??????????
      e.printStackTrace();
    }
  }

  private void runGpt(ChatMessage msg) {
    chatCompletionRequest.addMessage(msg);

    Task<ChatMessage> task = new Task<ChatMessage>() {
      @Override
      protected ChatMessage call() throws ApiProxyException {
        try {
          ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
          Choice result = chatCompletionResult.getChoices().iterator().next();
          chatCompletionRequest.addMessage(result.getChatMessage());
          System.out.println(result.getChatMessage().getContent()); // testing
          System.out.println(App.getController()); // testing
          Platform.runLater(
              () -> {
                App.getController().onNewChat(result.getChatMessage().getContent());
              });
          return result.getChatMessage();
        } catch (ApiProxyException e) {
          e.printStackTrace();
          return null;
        }
      }
    };

    Thread backgroundThread = new Thread(task);
    backgroundThread.start();
  }

  @FXML
  private void onSendExplanation(ActionEvent event) throws ApiProxyException, IOException {
    // evaluate user input using gpt and display the result only if player chooses
    // anthony
    // otherwise, straight to game over scene. load pre-written explanation instead
    // of using gpt
    if (suspectName.equals("Anthony")) {
      String message = txtaExplanation.getText().trim();
      if (message.isEmpty()) {
        return;
      }
      initialiseChat();
      ChatMessage msg = new ChatMessage("user", message);
      Guessing guessingState = (Guessing) App.getContext().getGuessingState();
      guessingState.getTimer().stop();

      try {
        App.setRoot("GameEnd");
      } catch (IOException e) {
        e.printStackTrace();
      }

      GameEndController gameEndController = (GameEndController) App.getController();
      gameEndController.showLoadingWheel(true);
      runGpt(msg);
    } else {
      // when the guess is wrong. load pre-written explanation
      try {
        App.setRoot("GameEnd");
      } catch (IOException e) {
        e.printStackTrace();
      }
      Guessing guessingState = (Guessing) App.getContext().getGuessingState();
      guessingState.getTimer().stop();
      GameEndController gameEndController = (GameEndController) App.getController();
      gameEndController.onWrongGuess(suspectName);
    }
  }

  @FXML
  private void handleOpenButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(true);
  }

  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    paneNoteWindow.setVisible(false);
  }

  @Override
  public void onNewChat(String chat) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }
}
