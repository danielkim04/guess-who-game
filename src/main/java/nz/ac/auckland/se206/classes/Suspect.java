package nz.ac.auckland.se206.classes;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class Suspect {
  private String name;
  private String job;
  private String role;
  private String note;
  private Rectangle rect;
  private ChatCompletionRequest chatCompletionRequest;
  private volatile ChatMessage currentChatMessage;
  private String chatHistory = "";

  public Suspect(String name, String job, String role, String note) {
    this.name = name;
    this.job = job;
    this.note = note;
    setRole(role);
  }

  public Suspect(Rectangle rect) {
    setRectProp(rect);
  }

  public Suspect(String name, Rectangle rect) {
    this.name = name;
    setRectProp(rect);
  }

  public Suspect(String name, String job, Rectangle rect) {
    this.name = name;
    this.job = job;
    setRectProp(rect);
  }

  private void setRectProp(Rectangle rect) {
    this.rect = rect;
  }

  public String getNote() {
    return (this.note);
  }

  public String getJob() {
    return (job);
  }

  public Rectangle getRect() {
    return (this.rect);
  }

  public void addChatHistory(String text) {
    this.chatHistory += text;
  }

  public void setChatHistory(String text) {
    this.chatHistory = text;
  }

  public String getChatHistory() {
    return (this.chatHistory);
  }

  public ChatCompletionRequest getCompletionRequest() {
    return (this.chatCompletionRequest);
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return (role);
  }

  public void setProfession() {
    if (this.chatCompletionRequest == null) {
      try {
        ApiProxyConfig config = ApiProxyConfig.readConfig();
        this.chatCompletionRequest =
            new ChatCompletionRequest(config)
                .setN(1)
                .setTemperature(0.2)
                .setTopP(0.5)
                .setMaxTokens(100);
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
    }
  }

  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("name", toString());
    map.put("profession", getJob());
    return PromptEngineering.getPrompt("chat.txt", map);
  }

  public String talk(ChatMessage msg) {

    if (msg == null) {
      if (!chatHistory.equals("")) {
        return (null);
      }
      msg = new ChatMessage("system", getSystemPrompt());
    }
    this.currentChatMessage = msg;

    Thread tttRequestThread =
        new Thread(
            () -> {
              try {
                this.currentChatMessage = runGpt(null);
                TextToSpeech.speak(this.currentChatMessage.getContent());
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              Platform.runLater(
                  () -> {
                    Object controller = App.getController();
                    if (controller.getClass().equals(Controller.class)) {
                      ((Controller) controller).onNewChat(this.currentChatMessage.getContent());
                    }
                  });
            });

    tttRequestThread.start();

    return (this.currentChatMessage.getContent());
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    if (msg != null) {
      this.currentChatMessage = msg;
    }
    chatCompletionRequest.addMessage(this.currentChatMessage);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public String toString() {
    return this.name;
  }
}
