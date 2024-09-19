package nz.ac.auckland.se206.classes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

  private Boolean interacted = false;

  // String that stores chat history
  private String chatHistory = "";

  private String promptFilename;

  public Suspect(String name, String role, String promptFilename) {
    this.name = name;
    setPromptFilename(promptFilename);
    setRole(role);
    initialiseChat();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return (this.name);
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return (role);
  }

  public void interacted() {
    this.interacted = true;
  }

  public Boolean getInteracted() {
    return (this.interacted);
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

  private void initialiseChat() {
    // initialise chat upon creation of suspect
    if (this.chatCompletionRequest == null) {
      try {
        ApiProxyConfig config = ApiProxyConfig.readConfig();
        this.chatCompletionRequest =
            new ChatCompletionRequest(config)
                .setN(1)
                .setTemperature(0.2)
                .setTopP(0.5)
                .setMaxTokens(100);
        String systemPrompt = loadSystemPrompt(); // load in system prompt
        chatCompletionRequest.addMessage(
            new ChatMessage("system", systemPrompt)); // add system prompt
      } catch (ApiProxyException | IOException | URISyntaxException e) {
        e.printStackTrace();
      }
    }
  }

  // Returns new GPT prompt message for GPT to process later
  // old implementation
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("name", toString()); // bar_name , gambler_name , victim_name
    map.put("role", getRole());
    return PromptEngineering.getPrompt("chat.txt", map);
  }

  // Loads system prompt from file
  private String loadSystemPrompt() throws IOException, URISyntaxException {
    System.out.println(this.promptFilename);
    URL resourceUrl =
        PromptEngineering.class.getClassLoader().getResource("prompts/" + this.promptFilename);
    if (resourceUrl == null) {
      System.out.println("Prompt file not found: " + this.promptFilename);
    }
    return new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
  }

  // Returns chat message from suspect
  public String talk(ChatMessage msg) {
    // not used in the current implementation
    if (msg == null) {
      if (!chatHistory.equals("")) {
        return (null);
      }
      // if no message is passed in, create a system prompt message
      msg = new ChatMessage("system", getSystemPrompt());
    }
    this.currentChatMessage = msg;

    Thread tttRequestThread =
        new Thread(
            () -> {
              try {
                this.currentChatMessage = runGpt(null);
                TextToSpeech.speak(this.currentChatMessage.getContent()); // we can remove this line
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

    // return chat message
    return (this.currentChatMessage.getContent());
  }

  // Returns GPT chat message
  private void runGptAsync(ChatMessage msg, Consumer<ChatMessage> callback)
      throws ApiProxyException {
    // used to display gpt response to suspect chat scenes.
    chatCompletionRequest.addMessage(
        new ChatMessage(
            "system", this.chatHistory)); // adds chat history? check if this works as intended
    if (msg != null) {
      this.currentChatMessage = msg;
      // add to chat history
      addChatHistory("User: " + msg.getContent() + "\n");
    }
    chatCompletionRequest.addMessage(this.currentChatMessage);

    Task<ChatMessage> task =
        new Task<ChatMessage>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              return result.getChatMessage(); // return response from GPT
            } catch (ApiProxyException e) {
              e.printStackTrace();
              return null;
            }
          }
        };

    task.setOnSucceeded(
        workerStateEvent -> {
          // upon receiving response from GPT, add to chat history and call callback
          ChatMessage response = task.getValue();
          addChatHistory("Suspect: " + response.getContent() + "\n");
          callback.accept(response);
        });

    Thread backgroundThread = new Thread(task);
    backgroundThread.start();
  }

  // Returns the name of the suspect whenever the suspect class is converted to
  // string
  @Override
  public String toString() {
    return this.name;
  }

  public void getResponse(String message, Consumer<String> callback) {
    // called by suspect room controllers to get response from GPT
    try {
      ChatMessage msg = new ChatMessage("user", message);
      // call runGptAsync to get response from GPT
      runGptAsync(
          msg,
          response -> {
            // send response to callback
            callback.accept(response.getContent());
          });
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // Returns GPT chat message
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    // not used in the current implementation
    if (msg != null) {
      this.currentChatMessage = msg;
    }
    chatCompletionRequest.addMessage(this.currentChatMessage); // adds chat history?
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      System.out.println(result.getChatMessage().getContent()); // for tetsing
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void setPromptFilename(String filename) {
    this.promptFilename = filename;
  }
}
