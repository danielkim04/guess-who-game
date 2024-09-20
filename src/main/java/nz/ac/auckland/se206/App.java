package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.classes.Controller;
import nz.ac.auckland.se206.classes.Suspect;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Investigating;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static GameStateContext context = new GameStateContext();
  private static FXMLLoader fxmlHandler;
  private static Map<String, Parent> sceneMap =
      new HashMap<>(); // stores the scenes that have been initialised
  private static Map<String, FXMLLoader> fxmlLoaderMap = new HashMap<>(); // stores the FXML loaders
  private static Map<String, Suspect> suspectMap = new HashMap<>();
  private static Map<MenuItem, String> locationMap = new HashMap<>();
  private static Suspect currentSuspect;

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
    currentSuspect = suspectMap.get(fxml);
    if (!sceneMap.containsKey(fxml)) {
      // if scene has not been initialised, load the FXML file and store it in the map
      Parent root = loadFxml(fxml);
      sceneMap.put(fxml, root);
    }
    // retrieve the FXML loader from the map
    fxmlHandler = fxmlLoaderMap.get(fxml);
    // retrieve the scene from the map and set it as the root
    scene.setRoot(sceneMap.get(fxml));
    if (context.getState() instanceof Investigating) {
      ((Investigating) context.getState()).sceneChange();
    }
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
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

  public static GameStateContext getContext() {
    return (context);
  }

  public static void setGameState(GameState gameState) {
    context.setState(gameState);
  }

  public static void resetAll() {
    context = new GameStateContext();
    // reset maps that retain the scenes and FXML loaders
    sceneMap.clear();
    fxmlLoaderMap.clear();
  }

  public static Suspect getCurrentSuspect() {
    return (currentSuspect);
  }

  public static void addToLocationMap(MenuItem loc, String scene) {
    locationMap.put(loc, scene);
  }

  public static void changeSceneMap(MenuItem loc) {
    try {
      setRoot(locationMap.get(loc));
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  public static Collection<Suspect> getSuspects() {
    return (suspectMap.values());
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    initaliseSuspectMap();
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

  private void initaliseSuspectMap() {
    suspectMap.put("SuspectOne", new Suspect("Mark", "Suspect", "Suspect1.txt"));
    suspectMap.put("SuspectTwo", new Suspect("Anthony", "Suspect", "Suspect2.txt"));
    suspectMap.put("SuspectThree", new Suspect("Susan", "Suspect", "Suspect3.txt"));
  }
}
