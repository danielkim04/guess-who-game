package nz.ac.auckland.se206.classes;

public class CharacterInteractionManager {

  // Singleton instance
  private static CharacterInteractionManager instance;

  // Booleans to track if a character has been talked to
  private boolean talkedToCharacter1;
  private boolean talkedToCharacter2;
  private boolean talkedToCharacter3;

  // Boolean to track if an interactable was clicked
  private boolean interactableClicked1;
  private boolean interactableClicked2;
  private boolean interactableClicked3;

  // Private constructor to prevent instantiation
  private CharacterInteractionManager() {
    reset(); // Initialize all values to false by default
  }

  // Public method to get the singleton instance
  public static CharacterInteractionManager getInstance() {
    if (instance == null) {
      instance = new CharacterInteractionManager();
    }
    return instance;
  }

  // Method to reset all interaction states
  public void reset() {
    this.talkedToCharacter1 = false;
    this.talkedToCharacter2 = false;
    this.talkedToCharacter3 = false;
    this.interactableClicked1 = false;
    this.interactableClicked2 = false;
    this.interactableClicked3 = false;
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

  public boolean isInteractableClicked1() {
    return interactableClicked1;
  }

  public boolean isInteractableClicked2() {
    return interactableClicked2;
  }

  public boolean isInteractableClicked3() {
    return interactableClicked3;
  }

  public void setInteractableClicked1(boolean interactableClicked) {
    this.interactableClicked1 = interactableClicked;
  }

  public void setInteractableClicked2(boolean interactableClicked) {
    this.interactableClicked2 = interactableClicked;
  }

  public void setInteractableClicked3(boolean interactableClicked) {
    this.interactableClicked3 = interactableClicked;
  }
}
