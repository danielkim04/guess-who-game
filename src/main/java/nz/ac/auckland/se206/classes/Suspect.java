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

/** Class responsible for suspect values. */
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

  /**
   * Constructor for the Suspect class.
   *
   * @param name the name of the suspect
   * @param role the role of the suspect
   * @param promptFilename the filename of the prompt
   */
  public Suspect(String name, String role, String promptFilename) {
    this.name = name;
    setPromptFilename(promptFilename);
    setRole(role);
    initialiseChat();
  }

  /**
   * Set name of suspect.
   *
   * @param name the name of the suspect
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get name of suspect.
   *
   * @return the name of the suspect
   */
  public String getName() {
    return (this.name);
  }

  /**
   * Set role of suspect.
   *
   * @param role the role of the suspect
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Get role of suspect.
   *
   * @return the role of the suspect
   */
  public String getRole() {
    return (role);
  }

  /** Set interacted to true */
  public void interacted() {
    this.interacted = true;
  }

  /**
   * Get interacted.
   *
   * @return interacted status of suspect
   */
  public Boolean getInteracted() {
    return (this.interacted);
  }

  /**
   * Set hitbox.
   *
   * @param rect the hitbox of the suspect
   */
  public void setRect(Rectangle rect) {
    this.rect = rect;
  }

  /**
   * Get hitbox.
   *
   * @return the hitbox of the suspect
   */
  public Rectangle getRect() {
    return (this.rect);
  }

  /**
   * Add chat history.
   *
   * @param text the text to be added to chat history
   */
  public void addChatHistory(String text) {
    this.chatHistory += text;
  }

  /**
   * Set chat history.
   *
   * @param text the text to be set as chat history
   */
  public void setChatHistory(String text) {
    this.chatHistory = text;
  }

  /**
   * Get chat history.
   *
   * @return the chat history
   */
  public String getChatHistory() {
    return (this.chatHistory);
  }

  /**
   * Get chat completion request.
   *
   * @return the chat completion request
   */
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

  /**
   * Get system prompt.
   *
   * @return the system prompt
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("name", toString()); // bar_name , gambler_name , victim_name
    map.put("role", getRole());
    return PromptEngineering.getPrompt("chat.txt", map);
  }

  /**
   * Load system prompt.
   *
   * @return the system prompt
   */
  private String loadSystemPrompt() throws IOException, URISyntaxException {
    System.out.println(this.promptFilename);
    URL resourceUrl =
        PromptEngineering.class.getClassLoader().getResource("prompts/" + this.promptFilename);
    if (resourceUrl == null) {
      System.out.println("Prompt file not found: " + this.promptFilename);
    }
    return new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
  }

  /**
   * Talk to the suspect.
   *
   * @param msg
   * @return
   */
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

  /**
   * Run GPT asynchronously.
   *
   * @param msg the chat message
   * @param callback the callback function
   */
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

  /**
   * Get name of suspect.
   *
   * @return the name of the suspect
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Get response from GPT.
   *
   * @param message the message
   * @param callback the callback function
   */
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

  /**
   * Run GPT.
   *
   * @param msg the chat message
   * @return the chat message
   * @throws ApiProxyException
   */
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

  /**
   * Set prompt filename.
   *
   * @param filename the filename of the prompt
   */
  public void setPromptFilename(String filename) {
    this.promptFilename = filename;
  }
}
