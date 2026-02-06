package tanjy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tanjy.Tanjy;

/**
 * Controller for the main GUI window.
 */
public class Gui extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Tanjy tanjy;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/steve.png"));
    private final Image botImage = new Image(this.getClass().getResourceAsStream("/images/villager.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Tanjy instance.
     */
    public void setTanjy(Tanjy t) {
        tanjy = t;

        dialogContainer.getChildren().add(
                DialogBox.getBotDialog(tanjy.getWelcomeMessage(), botImage)
        );
    }


    /**
     * Handles user input by creating user + bot dialog boxes.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = tanjy.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, botImage)
        );

        userInput.clear();

        if (tanjy.isExit()) {
            javafx.application.Platform.exit();
        }
    }
}
