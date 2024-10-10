package nz.ac.auckland.se206.classes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** Class responsible for playing audio */
public class AudioPlayer {

  private volatile List<Media> audioQueue = new ArrayList<Media>();
  private volatile MediaPlayer currentAudioPlayer;
  private Boolean playing = false;

  /**
   * Add audio to queue
   *
   * @param sound audio to be added to queue
   */
  public void addAudioToQueue(Media sound) {

    this.audioQueue.add(sound);

    if (!this.getIsPlaying()) {

      this.playAudioQueue();
      setIsPlaying(true);
    }
  }

  /** Play audio from queue */
  private void playNextInQueue() {
    // Change item to currently be played
    if (audioQueue.size() > 0) {
      Runnable onEndMediaRunnable = null;
      if (currentAudioPlayer != null) {
        onEndMediaRunnable = currentAudioPlayer.getOnEndOfMedia();
      }
      currentAudioPlayer = new MediaPlayer(audioQueue.remove(0));
      if (onEndMediaRunnable != null) {
        currentAudioPlayer.setOnEndOfMedia(onEndMediaRunnable);
      }

      currentAudioPlayer.play();
    } else {
      setIsPlaying(false);
    }
  }

  /**
   * Set playing status
   *
   * @param playing status of playing
   */
  private void setIsPlaying(Boolean playing) {
    this.playing = playing;
  }

  /**
   * Get playing status
   *
   * @return playing status
   */
  private Boolean getIsPlaying() {
    return (this.playing);
  }

  /** Play audio from queue */
  public synchronized void playAudioQueue() {
    // Start playing from queue
    Thread audioStoppedThread =
        new Thread(
            () -> {
              playNextInQueue();
            });

    Thread audioQueueThread =
        new Thread(
            () -> {
              if (currentAudioPlayer != null) {
                // Stop current audio
                currentAudioPlayer.stop();
              }
              Thread audioPlayThread = (new Thread(audioStoppedThread));
              if (audioQueue.size() > 0) {
                audioPlayThread.start();
                try {
                  audioPlayThread.join();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                currentAudioPlayer.setOnEndOfMedia(new Thread(audioStoppedThread));
              }
            });

    audioQueueThread.start();
  }
}
