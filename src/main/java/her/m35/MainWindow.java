package her.m35;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Herm35 herm35;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/UserIcon.png"));
    private final Image herm35Image = new Image(this.getClass().getResourceAsStream("/images/HERM35Icon.png"));

    /**
     * Initializes the HERM35 GUI.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the HERM35 instance */
    public void setHerm35(Herm35 herm35) {
        this.herm35 = herm35;
    }

    /**
     * Displays the introduction of HERM35.
     */
    public void showHerm35Introduction() {
        String[] response = herm35.getIntroduction();
        DialogBox herm35Dialog = DialogBox.getHerm35Dialog(response, herm35Image);
        herm35Dialog.prefWidthProperty().bind(scrollPane.widthProperty().subtract(30));
        dialogContainer.getChildren().addAll(
                herm35Dialog
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Herm35's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String[] response = herm35.getResponse(input);
        DialogBox userDialog = DialogBox.getUserDialog(input, userImage);
        userDialog.prefWidthProperty().bind(scrollPane.widthProperty().subtract(30));
        DialogBox herm35Dialog = DialogBox.getHerm35Dialog(response, herm35Image);
        herm35Dialog.prefWidthProperty().bind(scrollPane.widthProperty().subtract(30));
        dialogContainer.getChildren().addAll(
                userDialog,
                herm35Dialog
        );
        userInput.clear();

        if (herm35.getIsExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            Stage stage = (Stage) userInput.getScene().getWindow();
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> stage.close());
            pause.play();
        }
    }
}
