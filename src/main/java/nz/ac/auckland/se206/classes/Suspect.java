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
  private String role;

  // Varible that stores hitbox as a rectangle
  private Rectangle rect;

  // chat generation varibles
  private ChatCompletionRequest chatCompletionRequest;
  private volatile ChatMessage currentChatMessage;

  // String that stores chat history
  private String chatHistory = "";

  Suspect(String name, String role) {
    this.name = name;
    setRole(role);
    setProfession();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return (role);
  }

  public String getName() {
    return (this.name);
  }

  // Set hitbox
  public void setRect(Rectangle rect) {
    this.rect = rect;
  }

  // Get hitbox
  public Rectangle getRect() {
    return (this.rect);
  }

  // Append to chat history
  public void addChatHistory(String text) {
    this.chatHistory += text;
  }

  // Reset chat history to a single string
  public void setChatHistory(String text) {
    this.chatHistory = text;
  }

  // Returns full chat history of this characters responses
  public String getChatHistory() {
    return (this.chatHistory);
  }

  // Initalies GPT settings
  private void setProfession() {
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

  // Returns new GPT prompt message for GPT to process later
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("name", toString());
    map.put("role", getRole());
    return PromptEngineering.getPrompt("chat.txt", map);
  }

  // Returns chat message from suspect
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

  // Returns GPT chat message
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

  // Returns the name of the suspect whenever the suspect class is converted to string
  @Override
  public String toString() {
    return this.name;
  }
}
