package nz.ac.auckland.se206.classes;

/**
 * Class responsible for managing the interactions with the characters in the game
 */
public class Interactable {

  private String name;
  private String desc;

  Interactable(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  public String getName() {
    return (name);
  }

  public String getDesc() {
    return (desc);
  }
}
