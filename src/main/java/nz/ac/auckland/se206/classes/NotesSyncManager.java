package nz.ac.auckland.se206.classes;

/** Class responsible for notes sync. */
public class NotesSyncManager {

  // Static variable to store the shared notes
  private static String notesText = "";

  /**
   * This method is responsible for setting the notes text.
   *
   * @param notes the notes text
   */
  public static void setNotesText(String notes) {
    notesText = notes;
  }

  /** This method is responsible for appending the notes text. */
  public static String getNotesText() {
    return notesText;
  }

  /** This method is responsible for clearing the notes text. */
  public static void clearNotes() {
    notesText = ""; // Clear the static notes
  }
}
