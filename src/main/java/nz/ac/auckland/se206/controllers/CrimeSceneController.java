package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
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
 * Controller class for the room view. Handles user interactions within the room
 * where the user can
 * chat with customers and guess their profession.
 */
public class CrimeSceneController implements Controller {

  private boolean isLightToggled = false;
  private Cursor customCursor;

  private int moneyCollected = 0;
  private boolean hasClueBeenInspected = false;

  // Add these fields to the controller
  private Cursor dusterCursor;
  // Add these fields to the controller
  private Cursor grabCursor;

  @FXML
  private Rectangle rectClueBag;
  @FXML
  private Rectangle rectClueBook;
  @FXML
  private Rectangle rectClueNote;
  @FXML
  private ImageView rectBagCollection; // This is the collection rectangle for money
  @FXML
  private Label labelTimer;
  @FXML
  private Label moneyCounter; // Label to display the money collected
  @FXML
  private AnchorPane paneNoteWindow;
  @FXML
  private Rectangle rectCloseNotes;
  @FXML
  private Rectangle glow;
  @FXML
  private AnchorPane paneOpenChat;
  @FXML
  private AnchorPane bagInteractPane;
  @FXML
  private AnchorPane noteInteractPane;
  @FXML
  private Pane paneBase;
  @FXML
  private Pane hairSamplePane;
  @FXML
  private Pane fingerprintSamplePane;
  @FXML
  private Pane moneyCollectedPane;
  @FXML
  private Pane lightPane;
  @FXML
  private Pane fingerCollectedPane;
  @FXML
  private Pane cashbookPane;
  @FXML
  private Pane hairCollectedPane; // Pane that becomes visible when hair is collected
  @FXML
  private Label hairText; // Label for hair collection message
  @FXML
  private Label printLabel;
  @FXML
  private Label char1; // Mark 0/1
  @FXML
  private Label char2; // Anthony 0/1
  @FXML
  private Label char3; // Susan 0/1
  @FXML
  private Label char4; // Interactable 0/1
  @FXML
  private ImageView imgMap;
  @FXML
  private Button hairTest;
  @FXML
  private Button fingerprintTest;
  @FXML
  private Button bagExit;
  @FXML
  private Button cashbookExit;
  @FXML
  private Button checkBalance;
  @FXML
  private ImageView correct;
  @FXML
  private ImageView moneyOne;
  @FXML
  private ImageView moneyTwo;
  @FXML
  private ImageView moneyThree;
  @FXML
  private ImageView moneyFour;
  @FXML
  private ImageView moneyFive;
  @FXML
  private ImageView moneySix;
  @FXML
  private ImageView moneySeven;
  @FXML
  private ImageView moneyEight;
  @FXML
  private ImageView moneyNine;
  @FXML
  private ImageView moneyTen;
  @FXML
  private ImageView hair;
  @FXML
  private ImageView dark;
  @FXML
  private ImageView web1;
  @FXML
  private ImageView web2;
  @FXML
  private ImageView web3;
  @FXML
  private Button noteExit;
  @FXML
  private Button toggleLight;
  @FXML
  private ImageView blueLight;
  @FXML
  private ImageView fingerprint;
  @FXML
  private ImageView bagGlow;
  @FXML
  private ImageView notebookGlow;
  @FXML
  private ImageView noteGlow;
  @FXML
  private TextArea balanceArea;
  @FXML
  private TextArea notesText;
  @FXML
  private Pane labPane;
  @FXML
  private Label labLabel;
  @FXML
  private MenuItem menuSuspectTwo;
  @FXML
  private MenuItem menuSuspectThree;
  @FXML
  private MenuItem menuSuspectOne;
  @FXML
  private Button btnGuessNow;
  @FXML
  private Rectangle rectDisableButton;
  @FXML
  private ImageView imgGuessButton;
  @FXML
  private ImageView imgButtonNoColor;
  @FXML
  private AnchorPane paneCrimeSceneIntro;
  @FXML
  private ImageView imageCloseIntro;
  @FXML
  private AnchorPane mapMenuAnchorPane;

  // Variables to store the initial mouse click position
  private double initialX;
  private double initialY;

  /**
   * Initializes the room view. If it's the first time initialization, it will
   * provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {

    // Initially hide the bagGlow
    bagGlow.setVisible(false);

    // Set hover behavior for rectClueBag (show bagGlow and change cursor)
    rectClueBag.setOnMouseEntered(event -> {
      bagGlow.setVisible(true); // Show the glow
      rectClueBag.setCursor(javafx.scene.Cursor.HAND); // Change cursor to hand
    });

    rectClueBag.setOnMouseExited(event -> {
      bagGlow.setVisible(false); // Hide the glow
      rectClueBag.setCursor(javafx.scene.Cursor.DEFAULT); // Revert cursor to default
    });

    // Set hover behavior for rectClueBook (show notebookGlow and change cursor)
    rectClueBook.setOnMouseEntered(event -> {
      notebookGlow.setVisible(true); // Show the glow for notebook
      rectClueBook.setCursor(javafx.scene.Cursor.HAND); // Change cursor to hand
    });

    rectClueBook.setOnMouseExited(event -> {
      notebookGlow.setVisible(false); // Hide the glow for notebook
      rectClueBook.setCursor(javafx.scene.Cursor.DEFAULT); // Revert cursor to default
    });

    // Set hover behavior for rectClueNote (show noteGlow and change cursor)
    rectClueNote.setOnMouseEntered(event -> {
      noteGlow.setVisible(true); // Show the glow for note
      rectClueNote.setCursor(javafx.scene.Cursor.HAND); // Change cursor to hand
    });

    rectClueNote.setOnMouseExited(event -> {
      noteGlow.setVisible(false); // Hide the glow for note
      rectClueNote.setCursor(javafx.scene.Cursor.DEFAULT); // Revert cursor to default
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
    fingerprint.setOnMouseEntered(event -> fingerprint.setCursor(javafx.scene.Cursor.HAND));
    fingerprint.setOnMouseExited(event -> fingerprint.setCursor(javafx.scene.Cursor.DEFAULT));

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

  @FXML
  private void onNotesTextChanged() {
    // Update the notes in NotesSyncManager when the user edits the TextArea
    NotesSyncManager.setNotesText(notesText.getText());
  }

  @FXML
  private void onGuessNow(MouseEvent event) {
    try {
      App.getContext().getInvestigatingState().onGuessNow();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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

  @FXML
  private void onHandleOpenButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box opened");
    paneNoteWindow.setVisible(true);
  }

  @FXML
  private void handleCloseButtonClick(MouseEvent event) throws IOException {
    System.out.println("Box Closed");
    paneNoteWindow.setVisible(false);
  }

  @FXML
  private void handleBookClueClick(MouseEvent event) {
    // opens cash book clue
    cashbookPane.setVisible(true);
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
    System.out.println("clue opened");
  }

  @FXML
  private void onBagExit(MouseEvent event) {
    bagInteractPane.setVisible(false);
  }

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

  @FXML
  private void onCashBookExit(ActionEvent event) {
    // Hide the pane
    cashbookPane.setVisible(false);

    // Set the cursor back to the default system cursor
    paneBase.setCursor(Cursor.DEFAULT);
  }

  @FXML
  private void handleNoteClueClick(MouseEvent event) {
    // Show the noteInteractPane when rectClueNote is clicked
    noteInteractPane.setVisible(true);
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
    System.out.println("clue opened");
  }

  // New method to handle clicks on rectClueBag
  @FXML
  private void handleClueBagClick(MouseEvent event) {
    // Show the bagInteractPane when rectClueBag is clicked
    bagInteractPane.setVisible(true);
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
    System.out.println("clue opened");
  }

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

  @FXML
  private void onTestHair(MouseEvent event) {
    // Show the labPane
    labPane.setVisible(true);

    // Display message specific to the hair test
    displayLabTextSlowly("Hair contains brown eumelanin and belongs to a male...");
  }

  @FXML
  private void onFingerprintTestClick(MouseEvent event) {
    // Show the labPane
    labPane.setVisible(true);

    // Display message specific to the fingerprint test
    displayLabTextSlowly("The fingerprint tested likely belongs to Anthony...");
  }

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

  // Method to update the labels when the scene is opened
  public void onSceneOpened() {
    // Get the instance of the singleton to check the character states
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();

    // Update the labels based on the interaction status of the characters
    updateLabels(manager);
  }

  // Method to update the labels based on character interaction state
  private void updateLabels(CharacterInteractionManager manager) {
    // Update char1 (Mark)
    if (manager.isTalkedToCharacter1()) {
      char1.setText("Mark 1/1");
    } else {
      char1.setText("Mark 0/1");
    }

    // Update char2 (Anthony)
    if (manager.isTalkedToCharacter2()) {
      char2.setText("Anthony 1/1");
    } else {
      char2.setText("Anthony 0/1");
    }

    // Update char3 (Susan)
    if (manager.isTalkedToCharacter3()) {
      char3.setText("Susan 1/1");
    } else {
      char3.setText("Susan 0/1");
    }

    // Update char4 (Interactable)
    if (manager.isInteractableClicked()) {
      char4.setText("Interactable 1/1");
    } else {
      char4.setText("Interactable 0/1");
    }
  }

  // Handle interaction with Interactable
  public void onInteractableClicked() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    manager.setInteractableClicked(true);
    System.out.println("Interactable object clicked!");
    onSceneOpened();
  }

  // Check if all characters have been interacted with
  public void checkAllInteractions() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();

    if (manager.isTalkedToCharacter1() && manager.isTalkedToCharacter2() && manager.isTalkedToCharacter3()) {
      System.out.println("All characters have been interacted with.");
    } else {
      System.out.println("Some characters have not been interacted with yet.");
    }
  }

  // Example: Call this method to check if an interactable object has been clicked
  public void checkInteractable() {
    CharacterInteractionManager manager = CharacterInteractionManager.getInstance();
    if (manager.isInteractableClicked()) {
      System.out.println("The interactable object has been clicked.");
    } else {
      System.out.println("The interactable object has not been clicked yet.");
    }
  }

  private void makeImageViewDraggableWithCustomCursor(ImageView imageView) {
    // Handle mouse press event (when user clicks on the ImageView)
    imageView.setOnMousePressed(event -> {
      handleMousePressed(event, imageView);
      paneBase.setCursor(dusterCursor); // Set the custom duster cursor when pressed
    });

    // Handle mouse drag event (when user drags the ImageView)
    imageView.setOnMouseDragged(event -> handleMouseDragged(event, imageView));

    // Handle mouse release event (when user releases the ImageView)
    imageView.setOnMouseReleased(event -> {
      // Check if the mouse is still over the image after releasing
      if (imageView.contains(event.getX(), event.getY())) {
        // Keep the duster cursor if the mouse is still over the image
        paneBase.setCursor(dusterCursor);
      } else {
        // Otherwise, reset to default
        paneBase.setCursor(Cursor.DEFAULT);
      }
      checkMoneyIntersectionAndHide(imageView);
    });

    // Handle mouse entered event to set custom cursor
    imageView.setOnMouseEntered(event -> paneBase.setCursor(dusterCursor));

    // Handle mouse exited event to reset the cursor
    imageView.setOnMouseExited(event -> paneBase.setCursor(Cursor.DEFAULT));
  }

  // Method to display "Fingerprint Collected!" slowly in the printLabel
  private void displayFingerprintTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    printLabel.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame = new KeyFrame(
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

  // Method to hide the fingerCollectedPane after 2 seconds
  private void hideFingerprintPaneAfterDelay() {
    Timeline hidePaneTimeline = new Timeline(
        new KeyFrame(
            Duration.seconds(2), // Wait for 2 seconds
            ev -> fingerCollectedPane.setVisible(false) // Hide the pane
        ));
    hidePaneTimeline.play();
  }

  // Method to make ImageViews draggable with the grab cursor
  private void makeImageViewDraggable(ImageView imageView) {
    // Handle mouse press event (when user clicks on the ImageView)
    imageView.setOnMousePressed(event -> {
      handleMousePressed(event, imageView);
      paneBase.setCursor(grabCursor); // Set the custom grab cursor when pressed
    });

    // Handle mouse drag event (when user drags the ImageView)
    imageView.setOnMouseDragged(event -> handleMouseDragged(event, imageView));

    // Handle mouse release event (when user releases the ImageView)
    imageView.setOnMouseReleased(event -> {
      // Reset the cursor to default on release
      paneBase.setCursor(Cursor.DEFAULT);
      checkMoneyIntersectionAndHide(imageView);
    });

    // Handle mouse entered event to set custom cursor
    imageView.setOnMouseEntered(event -> paneBase.setCursor(grabCursor));

    // Handle mouse exited event to reset the cursor
    imageView.setOnMouseExited(event -> paneBase.setCursor(Cursor.DEFAULT));
  }

  // This method is triggered when the mouse is pressed on the ImageView
  private void handleMousePressed(MouseEvent event, ImageView imageView) {
    // Save the initial mouse position relative to the ImageView
    initialX = event.getSceneX() - imageView.getLayoutX();
    initialY = event.getSceneY() - imageView.getLayoutY();
  }

  // This method is triggered when the mouse is dragged
  private void handleMouseDragged(MouseEvent event, ImageView imageView) {
    // Update the position of the ImageView to follow the mouse
    imageView.setLayoutX(event.getSceneX() - initialX);
    imageView.setLayoutY(event.getSceneY() - initialY);
  }

  // Method to check if the currently dragged item is within the BagCollectionRect
  // area and hide it
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

  // Method to display the text letter by letter with a small delay and hide pane
  // after 3 seconds
  private void displayTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    hairText.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame = new KeyFrame(
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
          Timeline hidePaneTimeline = new Timeline(
              new KeyFrame(
                  Duration.seconds(2), // Wait for 2 seconds
                  ev -> hairCollectedPane.setVisible(false) // Hide the pane
          ));
          hidePaneTimeline.play();
        });

    // Start the timeline animation
    timeline.play();
  }

  // Method to update the MoneyCounter label
  private void updateMoneyCounter() {
    moneyCounter.setText("Money Collected: " + moneyCollected);
  }

  @Override
  public void onNewChat(String chat) {
    throw new UnsupportedOperationException("Unimplemented method 'onNewChat'");
  }

  @Override
  public void onTimerUpdate(String time) {
    labelTimer.setText(time);
  }

  // Method to check for intersection between a smaller hitbox of blueLight and
  // the fingerprint
  private void checkFingerprintIntersection() {
    // Define the smaller hitbox dimensions (adjust as needed)
    double hitboxWidth = blueLight.getFitWidth() * 0.3;
    double hitboxHeight = blueLight.getFitHeight() * 0.3;

    // Create a smaller rectangle (hitbox) around the center of blueLight
    double hitboxX = blueLight.getLayoutX() + (blueLight.getFitWidth() - hitboxWidth) / 2;
    double hitboxY = blueLight.getLayoutY() + (blueLight.getFitHeight() - hitboxHeight) / 2;

    // Create a temporary rectangle representing the hitbox
    javafx.geometry.Bounds blueLightHitbox = new javafx.geometry.BoundingBox(hitboxX, hitboxY, hitboxWidth,
        hitboxHeight);

    if (blueLightHitbox.intersects(fingerprint.getBoundsInParent())) {
      fingerprint.setOpacity(0.5); // Full opacity
    } else {
      fingerprint.setOpacity(0.0); // Fully hidden
    }
  }

  // Method to make the rectangle glow for 1 second
  private void makeRectangleGlow(Rectangle rect) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setColor(Color.YELLOW); // Set the glow color (adjust as needed)
    dropShadow.setRadius(20); // Set the initial glow radius

    // Apply the drop shadow effect to the rectangle
    rect.setEffect(dropShadow);

    // Create a timeline to animate the glow for 1 second (pulsing effect)
    Timeline glowTimeline = new Timeline(
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

  // Method to display text in labLabel letter by letter
  private void displayLabTextSlowly(String text) {
    final StringBuilder displayedText = new StringBuilder();
    labLabel.setText(""); // Clear the label initially

    Timeline timeline = new Timeline();
    for (int i = 0; i < text.length(); i++) {
      final int index = i;
      KeyFrame keyFrame = new KeyFrame(
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

  // Method to hide the labPane after 2 seconds
  private void hideLabPaneAfterDelay() {
    Timeline hidePaneTimeline = new Timeline(
        new KeyFrame(
            Duration.seconds(2), // Wait for 2 seconds
            ev -> labPane.setVisible(false) // Hide the pane
        ));
    hidePaneTimeline.play();
  }

  @FXML
  private void onSuspectOne(MouseEvent event) {
    changeSceneMap("SuspectOne");
  }

  @FXML
  private void onSuspectTwo(MouseEvent event) {
    changeSceneMap("SuspectTwo");
  }

  @FXML
  private void onSuspectThree(MouseEvent event) {
    changeSceneMap("SuspectThree");
  }

  private void changeSceneMap(String location) {
    try {
      App.setRoot(location);
    } catch (IOException e) {
      e.printStackTrace();
    }
    handleCloseMap();
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

  @Override
  public void unlockGuessBtn() {
    imgGuessButton.setVisible(true);
    imgButtonNoColor.setVisible(false);
    rectDisableButton.setVisible(false);
  }

  @FXML
  public void handleCloseIntroClick(MouseEvent event) {
    paneCrimeSceneIntro.setVisible(false);
  }
}
