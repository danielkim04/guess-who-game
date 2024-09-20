package nz.ac.auckland.se206.classes;

public class NotesSyncManager {

  // Static variable to store the shared notes
  private static String notesText = "";

  // Method to update notes
  public static void setNotesText(String notes) {
    notesText = notes;
  }

  // Method to retrieve the current notes
  public static String getNotesText() {
    return notesText;
  }

  // Method to clear the notes
  public static void clearNotes() {
    notesText = ""; // Clear the static notes
  }
}
