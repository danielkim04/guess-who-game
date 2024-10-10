package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.classes.CharacterInteractionManager;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.NotesSyncManager;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Investigating;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class CrimeSceneController implements Controller {

  private boolean isLightToggled = false;
  private Cursor customCursor;

  private int moneyCollected = 0;
  // Add these fields to the controller
  private Cursor dusterCursor;
  // Add these fields to the controller
  private Cursor grabCursor;

  @FXML private Rectangle rectClueBag;
  @FXML private Rectangle rectClueBook;
  @FXML private Rectangle rectClueNote;
  @FXML private ImageView rectBagCollection; // This is the collection rectangle for money
  @FXML private Label labelTimer;
  @FXML private Label moneyCounter; // Label to display the money collected
  @FXML private AnchorPane paneNoteWindow;
  @FXML private Rectangle rectCloseNotes;
  @FXML private ImageView glow;
  @FXML private AnchorPane paneOpenChat;
  @FXML private AnchorPane bagInteractPane;
  @FXML private AnchorPane noteInteractPane;
  @FXML private Pane paneBase;
  @FXML private Pane hairSamplePane;
  @FXML private Pane fingerprintSamplePane;
  @FXML private Pane moneyCollectedPane;
  @FXML private Pane lightPane;
  @FXML private Pane fingerCollectedPane;
  @FXML private Pane cashbookPane;
  @FXML private Pane hairCollectedPane; // Pane that becomes visible when hair is collected
  @FXML private Label hairText; // Label for hair collection message
  @FXML private Label printLabel;
  @FXML private Label char1;
  @FXML private Label char2;
  @FXML private Label char3;
  @FXML private Label char4;
  @FXML private ImageView imgMap;
  @FXML private Button hairTest;
  @FXML private Button fingerprintTest;
  @FXML private Button bagExit;
  @FXML private Button cashbookExit;
  @FXML private Button checkBalance;
  @FXML private ImageView correct;
  @FXML private ImageView moneyOne;
  @FXML private ImageView moneyTwo;
  @FXML private ImageView moneyThree;
  @FXML private ImageView moneyFour;
  @FXML private ImageView moneyFive;
  @FXML private ImageView moneySix;
  @FXML private ImageView moneySeven;
  @FXML private ImageView moneyEight;
  @FXML private ImageView moneyNine;
  @FXML private ImageView moneyTen;
  @FXML private ImageView hair;
  @FXML private ImageView dark;
  @FXML private ImageView web1;
  @FXML private ImageView web2;
  @FXML private ImageView web3;
  @FXML private Button noteExit;
  @FXML private Button toggleLight;
  @FXML private ImageView blueLight;
  @FXML private ImageView fingerprint;
  @FXML private ImageView bagGlow;
  @FXML private ImageView notebookGlow;
  @FXML private ImageView noteGlow;
  @FXML private TextArea balanceArea;
  @FXML private TextArea notesText;
  @FXML private Pane labPane;
  @FXML private Label labLabel;
  @FXML private MenuItem menuSuspectTwo;
  @FXML private MenuItem menuSuspectThree;
  @FXML private MenuItem menuSuspectOne;
  @FXML private Button btnGuessNow;
  @FXML private Rectangle rectDisableButton;
  @FXML private ImageView imgGuessButton;
  @FXML private ImageView imgButtonNoColor;
  @FXML private AnchorPane paneCrimeSceneIntro;
  @FXML private ImageView imageCloseIntro;
  @FXML private ImageView spider1;
  @FXML private ImageView spider2;
  @FXML private AnchorPane mapMenuAnchorPane;
  @FXML private Label markObjectiveLabel;
  @FXML private Label susanObjectiveLabel;
  @FXML private Label anthonyObjectiveLabel;
  @FXML private Label suspectObjectiveLabel;
  @FXML private Label clueObjectiveLabel;
  @FXML private Label allClueObjectiveLabel;

  // Variables to store the initial mouse click position
  private double initialX;
  private double initialY;

  private boolean web1Dragged;
  private boolean web2Dragged;
  private boolean web3Dragged;

  // Variable to track if the spider animation has been played
  private boolean animationPlayed;

  private CharacterInteractionManager manager = CharacterInteractionManager.getInstance();

  // Load your GIFs (Make sure the paths are correct)
  private Image spiderGif1 = new Image(getClass().getResourceAsStream("/gif/spiderLeft.gif"));
  private Image spiderGif2 = new Image(getClass().getResourceAsStream("/gif/spiderRight.gif"));

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {

    web1Dragged = false;
    web2Dragged = false;
    web3Dragged = false;

    animationPlayed = false;

    // Set the GIFs for both spiders but make them invisible at first
    spider1.setImage(spiderGif1);
    spider2.setImage(spiderGif2);

    spider1.setVisible(false);
    spider2.setVisible(false);

    // Initially hide the bagGlow
    bagGlow.setVisible(false);

    // Set hover behavior for rectClueBag (show bagGlow and change cursor)
    rectClueBag.setOnMouseEntered(
        event -> {
          bagGlow.setVisible(true); // Show the glow
          rectClueBag.setCursor(Cursor.HAND); // Change cursor to hand
        });

    rectClueBag.setOnMouseExited(
        event -> {
          bagGlow.setVisible(false); // Hide the glow
          rectClueBag.setCursor(Cursor.DEFAULT); // Revert cursor to default
        });

    // Set hover behavior for rectClueBook (show notebookGlow and change cursor)
    rectClueBook.setOnMouseEntered(
        event -> {
          notebookGlow.setVisible(true); // Show the glow for notebook
          rectClueBook.setCursor(Cursor.HAND); // Change cursor to hand
        });

    rectClueBook.setOnMouseExited(
        event -> {
          notebookGlow.setVisible(false); // Hide the glow for notebook
          rectClueBook.setCursor(Cursor.DEFAULT); // Revert cursor to default
        });

    // Set hover behavior for rectClueNote (show noteGlow and change cursor)
    rectClueNote.setOnMouseEntered(
        event -> {
          noteGlow.setVisible(true); // Show the glow for note
          rectClueNote.setCursor(Cursor.HAND); // Change cursor to hand
        });

    rectClueNote.setOnMouseExited(
        event -> {
          noteGlow.setVisible(false); // Hide the glow for note
          rectClueNote.setCursor(Cursor.DEFAULT); // Revert cursor to default
        });

    // Load the duster cursor image (ensure the path is correct)
    if (dusterCursor == null) {
      Image cursorImage = new Image(getClass().getResourceAsStream("/images/duster3.png"));
      dusterCursor = new ImageCursor(cursorImage);
    }

    // Load the grab cursor image (ensure the path is correct)
    if (grabCursor == null) {
      Image grabImage = new Image(getClass().getResourceAsStream("/images/grab2.png"));
      grabCursor = new ImageCursor(grabImage);
    }

    // Hide bagInteractPane and hairCollectedPane initially
    bagInteractPane.setVisible(false);
    hairCollectedPane.setVisible(false);
    hairSamplePane.setVisible(false);
    fingerprintSamplePane.setVisible(false);
    moneyCollectedPane.setVisible(false);
    correct.setVisible(false);

    hairSamplePane.toFront();
    fingerprintSamplePane.toFront();

    // Change cursor when hovering over fingerprint
    fingerprint.setOnMouseEntered(event -> fingerprint.setCursor(Cursor.HAND));
    fingerprint.setOnMouseExited(event -> fingerprint.setCursor(Cursor.DEFAULT));

    // Initialize money counter with zero
    updateMoneyCounter();

    // Add drag-and-drop functionality to Money
    makeImageViewDraggable(moneyOne);
    makeImageViewDraggable(moneyTwo);
    makeImageViewDraggable(moneyThree);
    makeImageViewDraggable(moneyFour);
    makeImageViewDraggable(moneyFive);
    makeImageViewDraggable(moneySix);
    makeImageViewDraggable(moneySeven);
    makeImageViewDraggable(moneyEight);
    makeImageViewDraggable(moneyNine);
    makeImageViewDraggable(moneyTen);

    // Add drag-and-drop functionality to webs with cursor change
    makeImageViewDraggableWithCustomCursor(web1);
    makeImageViewDraggableWithCustomCursor(web2);
    makeImageViewDraggableWithCustomCursor(web3);

    makeImageViewDraggable(hair);
    enableBothLightsToFollowCursor();
    lightPane.setVisible(false);
    blueLight.setMouseTransparent(true);
    dark.setMouseTransparent(true);
  }

  /**
   * This method prints the pressed key to command line.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * This method prints the released key to command line.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /** This method handles the mouse pressed event. */
  @FXML
  private void onNotesTextChanged() {
    // Update the notes in NotesSyncManager when the user edits the TextArea
    NotesSyncManager.setNotesText(notesText.getText());
  }

  /**
   * This method handles the click event on the guess button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onGuessNow(MouseEvent event) {
    try {
      App.getContext().getInvestigatingState().onGuessNow();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method handles the click event on the check balance button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onCheckBalance(ActionEvent event) {
    // Get the text from the balanceArea TextArea
    String balanceText = balanceArea.getText();

    // Check if the balance is "18000"
    if (balanceText.trim().equals("18000")) {
      // Action when balance is 18000, e.g., print a message
      System.out.println("Balance is exactly 18000!");
      correct.setVisible(true);
    } else {
      // Action when balance is not 18000
      System.out.println("Balance is not 18000. Current balance: " + balanceText);
      correct.setVisible(false);
    }
  }

  /**
   * This method handles the click event on the box open button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onHandleOpenButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box opened");
    paneNoteWindow.setVisible(true);
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box Closed");
    paneNoteWindow.setVisible(false);
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleBookClueClick(MouseEvent event) {
    // opens cash book clue
    cashbookPane.setVisible(true);
    manager.setInteractableClicked1(true);
    onInteractableClicked();

    // set clue interaction status
    Investigating investigatingState = (Investigating) App.getContext().getInvestigatingState();
    investigatingState.setClueInteractionStatus();
    GameState curGameState = App.getContext().getState();
    if (curGameState instanceof Investigating) {
      ((Investigating) curGameState).sceneChange();
    } else {
      System.out.println("Not investigating");
    }
    System.out.println("Clue 1 opened");
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onBagExit(MouseEvent event) {
    bagInteractPane.setVisible(false);
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onNoteExit(MouseEvent event) {
    // Hide the note interaction pane
    noteInteractPane.setVisible(false);

    // Set the cursor back to the default
    paneBase.setCursor(Cursor.DEFAULT);

    // Turn off the light
    lightPane.setVisible(false);

    // Update the light toggle state to reflect the light being off
    isLightToggled = false;
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onCashBookExit(ActionEvent event) {
    // Hide the pane
    cashbookPane.setVisible(false);

    // Set the cursor back to the default system cursor
    paneBase.setCursor(Cursor.DEFAULT);
  }

  /**
   * This method handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleNoteClueClick(MouseEvent event) {
    // Show the noteInteractPane when rectClueNote is clicked
    noteInteractPane.setVisible(true);
    manager.setInteractableClicked2(true);
    onInteractableClicked();

    // set clue interaction status
    Investigating investigatingState = (Investigating) App.getContext().getInvestigatingState();
    investigatingState.setClueInteractionStatus();
    GameState curGameState = App.getContext().getState();
    if (curGameState instanceof Investigating) {
      ((Investigating) curGameState).sceneChange();
    } else {
      System.out.println("Not investigating");
    }
    System.out.println("clue 2 opened");
  }

  /**
   * This method handles the click event on the bag clue button.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleClueBagClick(MouseEvent event) {
    // Show the bagInteractPane when rectClueBag is clicked
    bagInteractPane.setVisible(true);
    manager.setInteractableClicked3(true);
    onInteractableClicked();

    // set clue interaction status
    Investigating investigatingState = (Investigating) App.getContext().getInvestigatingState();
    investigatingState.setClueInteractionStatus();
    GameState curGameState = App.getContext().getState();
    if (curGameState instanceof Investigating) {
      ((Investigating) curGameState).sceneChange();
    } else {
      System.out.println("Not investigating");
    }
    System.out.println("clue 3 opened");
  }

  /**
   * This method handles the click on the light toggle button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onToggleLight(MouseEvent event) {
    // Toggle the visibility of the lightPane
    lightPane.setVisible(!lightPane.isVisible());

    // Toggle the light state
    isLightToggled = !isLightToggled;

    // Change cursor based on the light state
    if (isLightToggled) {
      // Load the custom cursor image (ensure the path is correct)
      if (customCursor == null) {
        Image cursorImage = new Image(getClass().getResourceAsStream("/images/torch2.png"));
        customCursor = new ImageCursor(cursorImage);
      }
      paneBase.setCursor(customCursor); // Set the custom cursor
    } else {
      paneBase.setCursor(Cursor.DEFAULT); // Set back to default cursor
    }
  }

  /**
   * This method handles the click on hair test button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onTestHair(MouseEvent event) {
    // Show the labPane
    labPane.setVisible(true);

    // Display message specific to the hair test
    displayLabTextSlowly("Hair contains brown eumelanin and belongs to a male...");
  }

  /**
   * This method handles the click on fingerprint test button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onFingerprintTestClick(MouseEvent event) {
    // Show the labPane
    labPane.setVisible(true);

    // Display message specific to the fingerprint test
    displayLabTextSlowly("The fingerprint tested likely belongs to Anthony...");
  }

  /** This method is responsible for the interactive mouse cursor. */
  @FXML
  private void enableBothLightsToFollowCursor() {
    // Define the offset values for both blueLight and dark
    double darkOffsetX = 24; // Move dark slightly to the right
    double darkOffsetY = 15; // Move dark slightly down
    double blueLightOffsetX = -10; // No offset for blueLight
    double blueLightOffsetY = -8; // No offset for blueLight

    paneBase.setOnMouseMoved(
        event -> {
          // Set the dark ImageView's position
          dark.setLayoutX(event.getSceneX() - (dark.getFitWidth() / 2) + darkOffsetX);
          dark.setLayoutY(event.getSceneY() - (dark.getFitHeight() / 2) + darkOffsetY);

          // Set the blueLight ImageView's position
          blueLight.setLayoutX(
              event.getSceneX() - (blueLight.getFitWidth() / 2) + blueLightOffsetX);
          blueLight.setLayoutY(
              event.getSceneY() - (blueLight.getFitHeight() / 2) + blueLightOffsetY);

          // Check for intersection between blueLight and fingerprint
          checkFingerprintIntersection();
        });
  }

  /**
   * This method checks if the blueLight ImageView intersects with the fingerprint ImageView.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleFingerprintClick(MouseEvent event) {
    // Print a message to the console when the fingerprint is clicked
    System.out.println("Fingerprint image clicked");
    makeRectangleGlow(glow);

    // Show the fingerCollectedPane when the fingerprint is clicked
    fingerCollectedPane.setVisible(true);
    fingerprintSamplePane.setVisible(true);

    // Display "Fingerprint Collected!" slowly
    displayFingerprintTextSlowly("Fingerprint Collected, Sample must be tested in the lab!");
  }

  /** This method is responsible for managing spider animations. */
  public void showSpiders() {
    Image spiderGif1 = new Image(getClass().getResourceAsStream("/gif/spiderLeft.gif"));
    Image spiderGif2 = new Image(getClass().getResourceAsStream("/gif/spiderRight.gif"));

    spider1.setImage(spiderGif1);
    spider2.setImage(spiderGif2);

    spider1.setOpacity(0);
    spider2.setOpacity(0);
    spider1.setVisible(true);
    spider2.setVisible(true);

    // Create fade-in animations for both spiders
    FadeTransition fadeInSpider1 = new FadeTransition(Duration.seconds(1), spider1);
    fadeInSpider1.setFromValue(0); // Start fully transparent
    fadeInSpider1.setToValue(1); // Fade to fully visible
    fadeInSpider1.play(); // Play the fade-in animation

    FadeTransition fadeInSpider2 = new FadeTransition(Duration.seconds(1), spider2);
    fadeInSpider2.setFromValue(0); // Start fully transparent
    fadeInSpider2.setToValue(1); // Fade to fully visible
    fadeInSpider2.play(); // Play the fade-in animation

    Timeline delayTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2),
                e -> {
                  // Create fade-out animations for both spiders
                  FadeTransition fadeOutSpider1 = new FadeTransition(Duration.seconds(1), spider1);
                  fadeOutSpider1.setFromValue(1); // Fully visible
                  fadeOutSpider1.setToValue(0); // Fully hidden
                  fadeOutSpider1.setOnFinished(
                      ev -> spider1.setVisible(false)); // Hide after fade out
                  fadeOutSpider1.play(); // Play the fade-out animation

                  FadeTransition fadeOutSpider2 = new FadeTransition(Duration.seconds(1), spider2);
                  fadeOutSpider2.setFromValue(1);
                  fadeOutSpider2.setToValue(0);
                  fadeOutSpider2.setOnFinished(ev -> spider2.setVisible(false));
                  fadeOutSpider2.play();
                }));

    delayTimeline.play();
  }

  /** This method is responsible for managing the glow effect on the rectangle. */
  public void onSceneOpened() {
    // Get the instance of the singleton to check the character states

    // Update the labels based on the interaction status of the characters
    updateLabels(manager);
  }

  /**
   * This method is responsible for updating the checklists based on the interaction status of the
   * characters.
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

  /** This method is responsible for responding to interactable clicks. */
  public void onInteractableClicked() {
    System.out.println("Interactable object clicked!");
    onSceneOpened();
  }

  /**
   * This method is responsible for checking all interaction status and moving onto next stage when
   * ready.
   */
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

  /** This method is called to check the state of the interactable objects. */
  public void checkInteractable() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
  }

  /**
   * This method allows the images to be draggable.
   *
   * @param imageView the ImageView to make glow
   */
  private void makeImageViewDraggableWithCustomCursor(ImageView imageView) {
    imageView.setOnMousePressed(
        event -> {
          handleMousePressed(event, imageView);
          paneBase.setCursor(dusterCursor); // Set the custom duster cursor when pressed
        });

    imageView.setOnMouseDragged(event -> handleMouseDragged(event, imageView));

    imageView.setOnMouseReleased(
        event -> {
          paneBase.setCursor(Cursor.DEFAULT);

          // Check if the ImageView being dragged is one of the webs
          if (imageView == web1) {
            web1Dragged = true;
          } else if (imageView == web2) {
            web2Dragged = true;
          } else if (imageView == web3) {
            web3Dragged = true;
          }

          // Check if all webs have been dragged
          checkAllWebsDragged();
        });

    imageView.setOnMouseEntered(event -> paneBase.setCursor(dusterCursor));
    imageView.setOnMouseExited(event -> paneBase.setCursor(Cursor.DEFAULT));
  }

  /** This method is responsible for initiating spider animation when all webs have been dragged. */
  // Check if all webs have been dragged and animation has not been played yet
  private void checkAllWebsDragged() {
    if (web1Dragged && web2Dragged && web3Dragged && !animationPlayed) {
      System.out.println("All webs have been dragged! Playing spider animation.");
      showSpiders(); // Play the spider animation

      // Set the flag to true to ensure the animation is only played once
      animationPlayed = true;
    }
  }

  /**
   * This method is responsible for displaying text slowly in the labLabel.
   *
   * @param text the text to display
   */
  private void displayFingerprintTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    printLabel.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.millis(75 * index), // Delay each letter by 100ms
              e -> {
                displayedText.append(text.charAt(index)); // Append the current letter
                printLabel.setText(displayedText.toString()); // Update the label with the new text
              });
      timeline.getKeyFrames().add(keyFrame);
    }

    // After the text has been fully displayed, hide the pane after 3 seconds
    timeline.setOnFinished(e -> hideFingerprintPaneAfterDelay());
    timeline.play();
  }

  /** This method is responsible for hiding fingerprint pane after a delay. */
  private void hideFingerprintPaneAfterDelay() {
    Timeline hidePaneTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2), // Wait for 2 seconds
                ev -> fingerCollectedPane.setVisible(false) // Hide the pane
                ));
    hidePaneTimeline.play();
  }

  /**
   * This method allows the images to be draggable.
   *
   * @param imageView the ImageView to make glow
   */
  private void makeImageViewDraggable(ImageView imageView) {
    // Handle mouse press event (when user clicks on the ImageView)
    imageView.setOnMousePressed(
        event -> {
          handleMousePressed(event, imageView);
          paneBase.setCursor(grabCursor); // Set the custom grab cursor when pressed
        });

    // Handle mouse drag event (when user drags the ImageView)
    imageView.setOnMouseDragged(event -> handleMouseDragged(event, imageView));

    // Handle mouse release event (when user releases the ImageView)
    imageView.setOnMouseReleased(
        event -> {
          // Reset the cursor to default on release
          paneBase.setCursor(Cursor.DEFAULT);
          checkMoneyIntersectionAndHide(imageView);
        });

    // Handle mouse entered event to set custom cursor
    imageView.setOnMouseEntered(event -> paneBase.setCursor(grabCursor));

    // Handle mouse exited event to reset the cursor
    imageView.setOnMouseExited(event -> paneBase.setCursor(Cursor.DEFAULT));
  }

  /**
   * This method is responsible for responding to a click.
   *
   * @param event the mouse event
   * @param imageView the ImageView to make glow
   */
  private void handleMousePressed(MouseEvent event, ImageView imageView) {
    // Save the initial mouse position relative to the ImageView
    initialX = event.getSceneX() - imageView.getLayoutX();
    initialY = event.getSceneY() - imageView.getLayoutY();
  }

  /**
   * This method is responsible for responding to drags using mouse.
   *
   * @param event the mouse event
   * @param imageView the ImageView to make glow
   */
  private void handleMouseDragged(MouseEvent event, ImageView imageView) {
    // Update the position of the ImageView to follow the mouse
    imageView.setLayoutX(event.getSceneX() - initialX);
    imageView.setLayoutY(event.getSceneY() - initialY);
  }

  /**
   * This method is responsible for checking interaction with the money clue.
   *
   * @param draggedItem the ImageView to check for intersection
   */
  private void checkMoneyIntersectionAndHide(ImageView draggedItem) {
    // Check if the dragged item intersects with the BagCollectionRect
    if (draggedItem.getBoundsInParent().intersects(rectBagCollection.getBoundsInParent())) {

      // If the dragged item is Hair, show the hairCollectedPane and animate the text
      if (draggedItem == hair) {
        System.out.println("Hair collected! No money gained.");
        makeRectangleGlow(glow);
        hairSamplePane.setVisible(true);
        draggedItem.setVisible(false);
        hairCollectedPane.setVisible(true); // Show hairCollectedPane

        // Display the hairText slowly, letter by letter
        displayTextSlowly("Hair Collected, Sample must be tested in the lab!");
      } else {
        if (draggedItem != web1 && draggedItem != web2 && draggedItem != web3) {
          // For other items (like money), hide the item and collect money
          draggedItem.setVisible(false);
          moneyCollectedPane.setVisible(true);
          moneyCollected += 1000;
          makeRectangleGlow(glow);
          updateMoneyCounter(); // Update the label when money is collected
          System.out.println(moneyCollected);
        }
      }
    }
  }

  /**
   * This method is responsible for displaying text slowly in the Label.
   *
   * @param text the text to display
   */
  private void displayTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    hairText.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.millis(75 * index), // Delay each letter by 100ms
              e -> {
                displayedText.append(text.charAt(index)); // Append the current letter
                hairText.setText(displayedText.toString()); // Update the label with the new text
              });
      timeline.getKeyFrames().add(keyFrame);
    }

    // After the text has been fully displayed, hide the pane after 2 seconds
    timeline.setOnFinished(
        e -> {
          Timeline hidePaneTimeline =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(2), // Wait for 2 seconds
                      ev -> hairCollectedPane.setVisible(false) // Hide the pane
                      ));
          hidePaneTimeline.play();
        });

    // Start the timeline animation
    timeline.play();
  }

  /** This method is responsible for updating the money counter label. */
  private void updateMoneyCounter() {
    moneyCounter.setText("Money Collected: " + moneyCollected);
  }

  /**
   * This method is not implemented it seems. But it has two usages not sure why.
   *
   * @param chat the chat to display
   */
  @Override
  public void onNewChat(String chat) {
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  /**
   * This method is called for updating the timer.
   *
   * @param time the time to display
   */
  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }

  /** This method is responsible for managing fingerprint interaction status. */
  private void checkFingerprintIntersection() {
    // Define the smaller hitbox dimensions (adjust as needed)
    double hitboxWidth = blueLight.getFitWidth() * 0.3;
    double hitboxHeight = blueLight.getFitHeight() * 0.3;

    // Create a smaller rectangle (hitbox) around the center of blueLight
    double hitboxX = blueLight.getLayoutX() + (blueLight.getFitWidth() - hitboxWidth) / 2;
    double hitboxY = blueLight.getLayoutY() + (blueLight.getFitHeight() - hitboxHeight) / 2;

    // Create a temporary rectangle representing the hitbox
    javafx.geometry.Bounds blueLightHitbox =
        new javafx.geometry.BoundingBox(hitboxX, hitboxY, hitboxWidth, hitboxHeight);

    if (blueLightHitbox.intersects(fingerprint.getBoundsInParent())) {
      fingerprint.setOpacity(0.5); // Full opacity
    } else {
      fingerprint.setOpacity(0.0); // Fully hidden
    }
  }

  /**
   * This method is responsible for making the rectangle glow when necessary.
   *
   * @param rect the rectangle to make glow
   */
  private void makeRectangleGlow(ImageView rect) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setColor(Color.YELLOW); // Set the glow color (adjust as needed)
    dropShadow.setRadius(20); // Set the initial glow radius

    // Apply the drop shadow effect to the rectangle
    rect.setEffect(dropShadow);

    // Create a timeline to animate the glow for 1 second (pulsing effect)
    Timeline glowTimeline =
        new Timeline(
            new KeyFrame(Duration.seconds(0), e -> dropShadow.setRadius(10)),
            new KeyFrame(Duration.seconds(0.5), e -> dropShadow.setRadius(20)),
            new KeyFrame(Duration.seconds(1), e -> dropShadow.setRadius(10)));

    // Ensure the timeline runs only once (for 1 second)
    glowTimeline.setCycleCount(1);

    // After the animation is finished, remove the glow effect
    glowTimeline.setOnFinished(e -> rect.setEffect(null));

    // Start the animation
    glowTimeline.play();
  }

  /**
   * This method is responsible for displaying text slowly in the labLabel.
   *
   * @param text the text to display
   */
  private void displayLabTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    labLabel.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.millis(75 * index), // Delay each letter by 100ms
              e -> {
                displayedText.append(text.charAt(index)); // Append the current letter
                labLabel.setText(displayedText.toString()); // Update the label with the new text
              });
      timeline.getKeyFrames().add(keyFrame);
    }

    // After the text has been fully displayed, hide the pane after 3 seconds
    timeline.setOnFinished(e -> hideLabPaneAfterDelay());
    timeline.play();
  }

  /** This method is responsible for hiding the lab pane after a delay. */
  private void hideLabPaneAfterDelay() {
    Timeline hidePaneTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2), // Wait for 2 seconds
                ev -> labPane.setVisible(false) // Hide the pane
                ));
    hidePaneTimeline.play();
  }

  /**
   * This method is responsible for changing scene to suspect one.
   *
   * @param event the mouse event
   */
  @FXML
  private void onSuspectOne(MouseEvent event) {
    changeSceneMap("SuspectOne");
  }

  /**
   * This method is responsible for changing scene to suspect one.
   *
   * @param event the mouse event
   */
  @FXML
  private void onSuspectTwo(MouseEvent event) {
    changeSceneMap("SuspectTwo");
  }

  /**
   * This method is responsible for changing scene to suspect three.
   *
   * @param event the mouse event
   */
  @FXML
  private void onSuspectThree(MouseEvent event) {
    changeSceneMap("SuspectThree");
  }

  /**
   * This method is responsible for changing maps depending on the location.
   *
   * @param location the location to change to
   */
  private void changeSceneMap(String location) {
    try {
      App.setRoot(location);
    } catch (IOException e) {
      e.printStackTrace();
    }
    handleCloseMap();
  }

  /**
   * This method is responsible for handling the mouse click event on the map.
   *
   * @param event the mouse event
   */
  @FXML
  private void handleMapClick(MouseEvent event) {
    Boolean menuStatus = mapMenuAnchorPane.isVisible();
    mapMenuAnchorPane.setDisable(menuStatus);
    mapMenuAnchorPane.setVisible(!menuStatus);
  }

  /** Handles the mouse clicked event on the close map button. */
  @FXML
  private void handleCloseMap() {
    mapMenuAnchorPane.setDisable(true);
    mapMenuAnchorPane.setVisible(false);
  }

  /** This method is responsible for unlocking the guess button. */
  @Override
  public void unlockGuessBtn() {
    imgGuessButton.setVisible(true);
    imgButtonNoColor.setVisible(false);
    rectDisableButton.setVisible(false);
  }

  /**
   * This method is responsible for closing the initial crime scene intro.
   *
   * @param event the mouse event
   */
  @FXML
  public void handleCloseIntroClick(MouseEvent event) {
    paneCrimeSceneIntro.setVisible(false);
  }
}
