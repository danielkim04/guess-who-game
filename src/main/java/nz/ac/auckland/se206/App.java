package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.classes.*;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;
import nz.ac.auckland.se206.states.GameState;

/**
 * This is the entry point of the JavaFX application. This class initializes and
 * runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static GameStateContext context = new GameStateContext();
  private static FXMLLoader fxmlHandler;
  private static Map<String, Parent> sceneMap = new HashMap<>(); // stores the scenes that have been initialised
  private static Map<String, FXMLLoader> fxmlLoaderMap = new HashMap<>(); // stores the FXML loaders

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    if (!sceneMap.containsKey(fxml)) {
      // if scene has not been initialised, load the FXML file and store it in the map
      Parent root = loadFxml(fxml);
      sceneMap.put(fxml, root);
    }
    // retrieve the FXML loader from the map
    fxmlHandler = fxmlLoaderMap.get(fxml);
    // retrieve the scene from the map and set it as the root
    scene.setRoot(sceneMap.get(fxml));
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that
   * the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    fxmlHandler = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    fxmlLoaderMap.put(fxml, fxmlHandler); // store the FXML loader in the map for later retrieval
    return (fxmlHandler.load());
  }

  /**
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event      the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  public static void openChat(MouseEvent event, String profession) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    Parent root = loader.load();

    ChatController chatController = loader.getController();
    chatController.setProfession(profession);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the
   * "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not
   *                     found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("Menu");
    scene = new Scene(root);
    stage.setTitle("Pi Masters Detective Training");
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(event -> handleWindowClose(event));
    root.requestFocus();
  }

  private void handleWindowClose(WindowEvent event) {
    FreeTextToSpeech.deallocateSynthesizer();
  }

  public static Controller getController() {
    return (fxmlHandler.getController());
  }

  public static GameStateContext getContext() {
    return (context);
  }

  public static void setGameState(GameState gameState) {
    context.setState(gameState);
  }

  public static void resetAll() {
    // reset maps that retain the scenes and FXML loaders
    sceneMap.clear();
    fxmlLoaderMap.clear();
  }
}
