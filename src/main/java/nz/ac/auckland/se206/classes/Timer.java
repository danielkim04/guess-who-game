package nz.ac.auckland.se206.classes;

// Class responsible for all timer
public class Timer {

  private Time time = new Time();

  // Value to count to in s
  private int timeoutCount;

  // Count interval in s
  private int interval = 1;

  // Amount to increment time by
  private int increment = 1;

  // Current value of counter
  private volatile Integer counter = 0;

  // Thread over ride
  private volatile boolean halt = false;

  // Task to execute
  private Thread executionThread;

  // Thread that waits interval and starts a new execution thread
  private Thread timerThread =
      new Thread(
          () -> {
            while ((this.time.getTime() <= timeoutCount) & (!halt)) {
              if (executionThread != (null)) {
                (new Thread(executionThread)).start();
              } else {
                System.out.println("Null thread");
              }
              try {
                Thread.sleep(interval);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              this.count();
            }
            stop();
            if (executionThread != (null)) {
              (new Thread(executionThread)).start();
            }
            // App.getContext().handleTimeOut();
          });

  // Current timer thread executing
  private Thread activeTimerThread = null;

  // Initialises class
  public Timer(int timeoutCount) {
    setTimeOut(timeoutCount);
  }

  // Resets timer
  public void clear() {
    this.counter = 0;
    this.activeTimerThread = new Thread(timerThread);
    this.halt = false;
  }

  // Iterate timer
  private void count() {
    this.counter += increment;
    if (increment > 0) {
      this.time.addSecs(increment);
    } else if (increment < 0) {
      this.time.subSecs(increment * -1);
    }
  }

  // Returns value to count to
  public Integer getTimeOut() {
    return (this.timeoutCount);
  }

  // Returns true if the timer is not counting
  public Boolean getStopped() {
    return (halt);
  }

  // Returns current count value
  public Integer getCount() {
    return (this.counter);
  }

  // Begins timer
  public void start() {

    if (this.activeTimerThread != null) {
      this.stop();
      try {
        this.activeTimerThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (executionThread != (null)) {
      (new Thread(executionThread)).start();
    }

    this.activeTimerThread = new Thread(timerThread);
    this.activeTimerThread.setDaemon(true);
    this.activeTimerThread.start();
  }

  // Stop timer
  public void stop() {
    this.halt = true;
  }

  // Sets timeout of timer
  public void setTimeOut(int timeoutCount) {
    this.timeoutCount = timeoutCount;
    this.time.setTime(timeoutCount);
  }

  // Sets thread to execute when after an interval
  public void setExecution(Thread exeuctableThread) {
    this.executionThread = exeuctableThread;
  }
}
