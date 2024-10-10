package nz.ac.auckland.se206.classes;

/** Timer class that counts down from a given value and executes a thread after a given interval. */
public class Timer {

  private Time time = new Time();

  // Value to count to in s
  private int timeoutCount;

  // Count interval in s
  private int interval = 1;

  // Amount to increment time by
  private int increment = -1;

  // Current value of counter
  private volatile Integer counter = 0;

  // Thread over ride
  private volatile boolean halt = false;

  // Task to execute
  private Thread executionThread;

  private Thread timeOutThread;

  // Thread that waits interval and starts a new execution thread
  private Thread timerThread =
      new Thread(
          () -> {
            while (!halt) {
              if (executionThread != (null)) {
                (new Thread(executionThread)).start();
              } else {
                System.out.println("Null thread");
              }
              try {
                Thread.sleep(interval * 1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              if (this.time.getTime() <= 0) {
                stop();
                handleTimeOut();
              }

              this.count();
            }
          });

  // Current timer thread executing
  private Thread activeTimerThread = null;

  /** Constructor for the timer. */
  public Timer(int timeoutCount) {
    setTimeOut(timeoutCount);
  }

  /** Constructor for the timer. */
  private void handleTimeOut() {
    System.out.println("timeout");
    if (this.timeOutThread != null) {
      this.activeTimerThread = new Thread(timeOutThread);
      this.activeTimerThread.setDaemon(true);
      this.activeTimerThread.start();
    }
  }

  /** Clears the timer. */
  public void clear() {
    this.counter = 0;
    this.activeTimerThread = new Thread(timerThread);
    this.halt = false;
  }

  /** Increments the counter. */
  private void count() {
    this.counter += increment;
    if (increment > 0) {
      this.time.addSecs(increment);
    } else if (increment < 0) {
      this.time.subSecs(increment * -1);
    }
  }

  /** Sets the interval of the timer. */
  public Time getTime() {
    return (this.time);
  }

  /** Sets the interval of the timer. */
  public Integer getTimeOut() {
    return (this.timeoutCount);
  }

  /** Sets the interval of the timer. */
  public Boolean getStopped() {
    return (halt);
  }

  /** Sets the interval of the timer. */
  public Integer getCount() {
    return (this.counter);
  }

  /** Starts the timer. */
  public void start() {
    // Check if thread exists
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
    // Set teh timer thread
    this.activeTimerThread = new Thread(timerThread);
    this.activeTimerThread.setDaemon(true);
    this.activeTimerThread.start();
  }

  /** Stops the timer. */
  public void stop() {
    this.halt = true;
    System.out.println("Stopping timer");
  }

  /**
   * Sets the interval of the timer.
   *
   * @param timeoutCount the interval to set
   */
  public void setTimeOut(int timeoutCount) {
    this.timeoutCount = timeoutCount;
    this.time.setTime(timeoutCount);
  }

  /**
   * Sets the interval of the timer.
   *
   * @param exeuctableThread the interval to set
   */
  public void setExecution(Thread exeuctableThread) {
    this.executionThread = exeuctableThread;
  }

  /**
   * Sets the interval of the timer.
   *
   * @param timeOutThread the interval to set
   */
  public void setTimeOutThread(Thread timeOutThread) {
    this.timeOutThread = timeOutThread;
  }
}
