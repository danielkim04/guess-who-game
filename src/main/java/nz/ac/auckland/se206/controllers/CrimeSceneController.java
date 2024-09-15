package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;

public class CrimeSceneController {

    @FXML private Rectangle rectClueBag;
    @FXML private Rectangle rectClueBook;
    @FXML private Rectangle rectClueNote;
    @FXML private Label labelTimer;
    @FXML private AnchorPane paneNoteWindow;
    @FXML private Rectangle rectCloseNotes;
    @FXML private AnchorPane paneOpenChat;
    @FXML private AnchorPane bagInteractPane;
    @FXML private Pane paneBase;
    @FXML private ImageView imgMap;
    @FXML private ImageView Money10; // ImageView for the money that will be dragged

    private static GameStateContext context = new GameStateContext();

    // Variables to store the initial mouse click position
    private double initialX;
    private double initialY;

    /**
     * Initializes the room view. If it's the first time initialization, it will provide instructions
     * via text-to-speech.
     */
    @FXML
    public void initialize() {
        // Hide bagInteractPane initially
        bagInteractPane.setVisible(false);

        // Add drag-and-drop functionality to Money10
        makeImageViewDraggable(Money10);
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
    private void handleRectangleClick(MouseEvent event) throws IOException {
        Rectangle clickedRectangle = (Rectangle) event.getSource();
        // to be implemented
    }

    /**
     * Handles the guess button click event.
     *
     * @param event the action event triggered by clicking the guess button
     * @throws IOException if there is an I/O error
     */
    @FXML
    private void handleGuessClick(ActionEvent event) throws IOException {
        context.handleGuessClick();
    }

    @FXML
    private void handleOpenButtonClick(MouseEvent event) throws IOException {
        paneNoteWindow.setVisible(true);
    }

    @FXML
    private void handleCloseButtonClick(MouseEvent event) throws IOException {
        paneNoteWindow.setVisible(false);
    }

    @FXML
    private void handleMapClick(MouseEvent event) throws IOException {
        // to be implemented
    }

    // New method to handle clicks on rectClueBag
    @FXML
    private void handleClueBagClick(MouseEvent event) {
        // Show the bagInteractPane when rectClueBag is clicked
        bagInteractPane.setVisible(true);
    }

    // Method to make an ImageView draggable
    private void makeImageViewDraggable(ImageView imageView) {
        // Handle mouse press event (when user clicks on the ImageView)
        imageView.setOnMousePressed(event -> handleMousePressed(event, imageView));
        
        // Handle mouse drag event (when user drags the ImageView)
        imageView.setOnMouseDragged(event -> handleMouseDragged(event, imageView));
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
}
