package nz.ac.auckland.se206.classes;

/** Class responsible for managing the time in the game. */
public class Time {

  // Creates inital varibles for time
  private Integer minutes;
  private Integer seconds;

  /** This class is responsible for managing the time in the game. */
  public Time() {
    // Initalises time varibles to 0
    this.minutes = 0;
    this.seconds = 0;
  }

  /**
   * This method is responsible for getting the time in mins.
   *
   * @return Time in minutes
   */
  public Integer getMins() {
    return (this.minutes);
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @return Time in seconds
   */
  public Integer getSecs() {
    return (this.seconds);
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @return Time in seconds
   */
  public Integer getTime() {
    return (this.minutes * 60 + this.seconds);
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param mins the minutes to set
   */
  public void setMins(Integer mins) {
    this.minutes = mins % 60;
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param secs the seconds to set
   */
  public void setSecs(Integer secs) {
    seconds = secs % 60;
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param secs the seconds to set
   */
  public void setTime(Integer secs) {
    // Adds minute periods within the input
    if (secs >= 60) {
      setMins(secs / 60);
    } else if (secs <= 0) {
      setSecs(0);
      return;
    } else if (secs < 60) {
      setMins(0);
    }
    // Sets remaining seconds
    setSecs(secs);
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param mins the minutes to set
   * @param secs the seconds to set
   */
  public void setTime(Integer mins, Integer secs) {
    // Sets time given a minute and second input
    if (mins > 0) {
      setMins(mins);
    } else {
      setMins(0);
    }

    if (secs > 0) {
      setSecs(secs);
    } else {
      setSecs(0);
    }
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param mins the minutes to add
   */
  public void addMins(Integer mins) {
    // Adds a given number of minutes to the time
    minutes += mins % 60;
    minutes %= 60;
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param secs the seconds to add
   */
  public void addSecs(Integer secs) {
    // Adds a given number of seconds to the time
    if (secs >= 60) {
      seconds += secs % 60;
      addMins(secs / 60);
    } else if (secs > 0) {
      seconds += secs;
    } else {
      return;
    }
    while (seconds >= 60) {
      seconds %= 60;
      addMins(1);
    }
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param mins the minutes to subtract
   */
  public void subMins(Integer mins) {
    // Subtracts an amount of minutes from the time
    minutes -= mins;
    if (minutes < 0) {
      minutes = 0;
      seconds = 0;
    }
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @param secs the seconds to subtract
   */
  public void subSecs(Integer secs) {
    // Subtracts an amount of seconds from the time
    seconds -= secs;
    if (seconds < 0) {
      subMins(seconds * -1);
      seconds = (seconds + 60) % 60;
    }
  }

  /**
   * This method is responsible for getting the time in seconds.
   *
   * @return Time in string format
   */
  @Override
  public String toString() {
    return (this.minutes.toString() + ":" + (this.seconds / 10) + (this.seconds % 10));
  }
}
