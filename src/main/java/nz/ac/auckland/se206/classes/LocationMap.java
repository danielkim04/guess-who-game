package nz.ac.auckland.se206.classes;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.*;

public class LocationMap {
  private static Map<MenuItem, String> locationMap = new HashMap<>();

  public static void add(MenuItem loc, String scene) {
    locationMap.put(loc, scene);
  }

  public static String getScene(MenuItem loc) {
    return (locationMap.get(loc));
  }
}
