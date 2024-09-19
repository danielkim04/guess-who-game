package nz.ac.auckland.se206.classes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {

  private volatile List<Media> audioQueue = new ArrayList<Media>();
  private volatile MediaPlayer currentAudioPlayer;
  private Boolean playing = false;

  public AudioPlayer() {
  }

  // Adds media to queue
  public void addAudioToQueue(Media sound) {

    this.audioQueue.add(sound);

    if (!this.getIsPlaying()) {

      this.playAudioQueue();
      setIsPlaying(true);
    }
  }

  // Change item to currently be played
  private void playNextInQueue() {

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

    return;
  }

  private void setIsPlaying(Boolean playing) {
    this.playing = playing;
  }

  private Boolean getIsPlaying() {
    return (this.playing);
  }

  // Start playing from queue
  public synchronized void playAudioQueue() {

    Thread audioStoppedThread = new Thread(
        () -> {
          playNextInQueue();
        });

    Thread audioQueueThread = new Thread(
        () -> {
          if (currentAudioPlayer != null) {
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
    return;
  }
}
