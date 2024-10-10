package nz.ac.auckland.se206.classes;

/**
 * Class responsible for managing the interactions with the characters in the game
 */
public class CharacterInteractionManager {

  // Singleton instance
  private static CharacterInteractionManager instance;

  // Booleans to track if a character has been talked to
  private boolean talkedToCharacter1;
  private boolean talkedToCharacter2;
  private boolean talkedToCharacter3;

  // Boolean to track if an interactable was clicked
  private boolean interactableClicked;

  // Private constructor to prevent instantiation
  private CharacterInteractionManager() {
    reset(); // Initialize all values to false by default
  }

  /**
   * Returns the singleton instance of the CharacterInteractionManager
   *
   * @return the singleton instance of the CharacterInteractionManager
   */
  public static CharacterInteractionManager getInstance() {
    if (instance == null) {
      instance = new CharacterInteractionManager();
    }
    return instance;
  }

  /**
   * Resets all the booleans to false
   */
  public void reset() {
    this.talkedToCharacter1 = false;
    this.talkedToCharacter2 = false;
    this.talkedToCharacter3 = false;
    this.interactableClicked = false;
  }

  // Getters and setters for the booleans
  public boolean isTalkedToCharacter1() {
    return talkedToCharacter1;
  }

  public void setTalkedToCharacter1(boolean talkedToCharacter1) {
    this.talkedToCharacter1 = talkedToCharacter1;
  }

  public boolean isTalkedToCharacter2() {
    return talkedToCharacter2;
  }

  public void setTalkedToCharacter2(boolean talkedToCharacter2) {
    this.talkedToCharacter2 = talkedToCharacter2;
  }

  public boolean isTalkedToCharacter3() {
    return talkedToCharacter3;
  }

  public void setTalkedToCharacter3(boolean talkedToCharacter3) {
    this.talkedToCharacter3 = talkedToCharacter3;
  }

  public boolean isInteractableClicked() {
    return interactableClicked;
  }

  public void setInteractableClicked(boolean interactableClicked) {
    this.interactableClicked = interactableClicked;
  }
}
