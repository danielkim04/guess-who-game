package nz.ac.auckland.se206.classes;

public interface Controller {

  // Runs when ai message has been generated for chat box
  public void onNewChat(String chat);

  // Runs when timer iterates
  public void onTimerUpdate();

  // Runs when timer iterates and gives current time
  public void onTimerUpdate(Time time);

  // Runs when timer has timed out
  public void onTimerStop();
}
