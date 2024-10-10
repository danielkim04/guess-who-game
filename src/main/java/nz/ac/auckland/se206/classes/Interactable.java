package nz.ac.auckland.se206.classes;

/** Class responsible for managing the interactions with the characters in the game. */
public class Interactable {

  private String name;
  private String desc;

  /**
   * Constructor for the Interactable class.
   *
   * @param name the name of the interactable
   * @param desc the description of the interactable
   */
  Interactable(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  /**
   * Returns the name of the interactable.
   *
   * @return the name of the interactable
   */
  public String getName() {
    return (name);
  }

  /**
   * Returns the description of the interactable.
   *
   * @return the description of the interactable
   */
  public String getDesc() {
    return (desc);
  }
}
