package nz.ac.auckland.se206.classes;

/** Interface for the controller. */
public interface Controller {

  // Runs when ai message has been generated for chat box
  public void onNewChat(String chat);

  // Runs when timer iterates and gives current time
  public void onTimerUpdate(String time);

  public void unlockGuessBtn();
}
