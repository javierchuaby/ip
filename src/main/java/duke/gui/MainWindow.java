package duke.gui;

import java.util.Objects;

import duke.MrMoon;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI window of the MrMoon chatbot application. Handles user input,
 * displays chat messages, and manages the application lifecycle. Supports automatic exit
 * functionality and welcome message display.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage =
        new Image(
            Objects.requireNonNull(
                this.getClass()
                    .getResourceAsStream("/images/icons8-male-user-480.png")));
    private final Image mrMoonImage =
        new Image(
            Objects.requireNonNull(
                this.getClass().getResourceAsStream("/images/icons8-user-480.png")));
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private MrMoon mrMoon;
    private boolean isExited = false;

    private boolean waitingForClearConfirmation = false;

    /**
     * Initializes the main window components and sets up scroll pane binding.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the MrMoon instance into this controller.
     *
     * @param m The MrMoon chatbot instance to use
     */
    public void setMrMoon(MrMoon m) {
        mrMoon = m;
        showWelcomeMessage();
    }

    /**
     * Displays the welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        String welcomeMessage =
            "Hello! I'm Mr Moon, your personal Task Manager!\nWhat can I do for you?";
        dialogContainer.getChildren().add(DialogBox.getMrMoonDialog(welcomeMessage, mrMoonImage));
    }

    /**
     * Handles user input from the text field and generates appropriate responses. Manages the chat
     * flow and application exit logic.
     */
    @FXML
    private void handleUserInput() {
        if (isExited) {
            return;
        }

        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        if (waitingForClearConfirmation) {
            String response = handleClearConfirmation(input.toLowerCase());
            dialogContainer
                .getChildren()
                .addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getMrMoonDialog(response, mrMoonImage));
            userInput.clear();
            return;
        }

        String response = getResponse(input);

        if (input.toLowerCase().equals("clear") && mrMoon.getTasks().size() > 0) {
            waitingForClearConfirmation = true;
            response = "Are you sure you want to clear all tasks? (yes/no)";
        }

        dialogContainer
            .getChildren()
            .addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMrMoonDialog(response, mrMoonImage));
        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            isExited = true;
            userInput.setDisable(true);
            sendButton.setDisable(true);

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(
                event -> {
                    Platform.exit();
                    System.exit(0);
                });
            delay.play();
        }
    }

    /**
     * Handles user responses to clear confirmation prompts. Processes "yes" to clear tasks, "no" to
     * cancel, and asks for clarification otherwise.
     */
    private String handleClearConfirmation(String response) {
        switch (response) {
        case "yes":
            mrMoon.getTasks().clear();
            waitingForClearConfirmation = false;
            return "All tasks have been cleared!";
        case "no":
            waitingForClearConfirmation = false;
            return "Clear operation cancelled.";
        default:
            return "Please type 'yes' or 'no'.";
        }
    }

    /**
     * Generates a response to user input using the MrMoon chatbot.
     *
     * @param input The user's input string
     * @return The chatbot's response string
     */
    private String getResponse(String input) {
        try {
            return mrMoon.getResponse(input);
        } catch (Exception e) {
            return "Oops! Something went wrong: " + e.getMessage();
        }
    }
}
