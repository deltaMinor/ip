package her.m35;

import java.io.IOException;
import java.util.Collections;

import her.m35.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    /**
     * dialog changed to type TextFlow to allow for coloured text highlighting feature, based on result from ChatGPT.
     */
    @FXML
    private TextFlow dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DialogBox(String message, Image img) {
        this();
        dialog.getChildren().add(new Text(message));
        dialog.getStyleClass().add("dialog-box");
        displayPicture.setImage(img);
    }

    private DialogBox(String[] message, Image img) {
        this();
        //Form dialog text with coloured highlighting based on content of text
        for (String segment : message) {
            if (segment.equals(Herm35.NAME)) {
                dialog.getChildren().add(styleName(segment));
            } else if (segment.startsWith("$")) {
                dialog.getChildren().add(styleItalic(segment.substring(1)));
            } else if (segment.startsWith("[")) {
                Text[] taskParts = styleTask(segment);
                for (Text taskPart : taskParts) {
                    dialog.getChildren().add(taskPart);
                }
            } else if (segment.startsWith("#")) {
                Text[] tags = styleTags(segment);
                for (Text tag : tags) {
                    dialog.getChildren().add(tag);
                }
            } else if (segment.startsWith("Warning: ")) {
                dialog.getChildren().add(styleError(segment));
            } else if (segment.startsWith("Error: ")) {
                dialog.getChildren().add(styleError(segment));
            } else {
                dialog.getChildren().add(new Text(segment));
            }
        }
        dialog.getStyleClass().add("dialog-box");
        displayPicture.setImage(img);
    }

    private Text styleName(String name) {
        Text nameText = new Text(name);
        nameText.getStyleClass().add("name-text");
        return nameText;
    }

    private Text styleItalic(String string) {
        Text italicText = new Text(string);
        italicText.getStyleClass().add("italics");
        return italicText;
    }

    private Text styleError(String error) {
        Text errorText = new Text(error);
        errorText.getStyleClass().add("error-text");
        return errorText;
    }

    private Text styleWarning(String warning) {
        Text warningText = new Text(warning);
        warningText.getStyleClass().add("warning-text");
        return warningText;
    }

    private Text[] styleTags(String tagsString) {
        String[] tags = tagsString.split("(?=#)|(?=,)");
        Text[] tagTexts = new Text[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagTexts[i] = new Text(tags[i]);
            if (tags[i].startsWith("#")) {
                tagTexts[i].getStyleClass().add("tag-text");
            }
        }
        return tagTexts;
    }

    private Text[] styleTask(String task) {
        boolean taskDone = String.valueOf(task.charAt(Task.MARK_POSITION)).equals(Task.DONE_MARK);
        if (taskDone) {
            return styleDoneTask(task);
        } else {
            return new Text[] {styleUndoneTask(task)};
        }
    }

    private Text[] styleDoneTask(String doneTask) {
        Text text1 = new Text(doneTask.substring(0, Task.MARK_POSITION));
        Text doneMarkText = new Text(Task.DONE_MARK);
        doneMarkText.getStyleClass().add("done-mark");
        Text text2 = new Text(doneTask.substring(Task.MARK_POSITION + 1));
        switch (doneTask.charAt(Task.TYPE_POSITION)) {
        case 'T':
            text1.getStyleClass().add("todo-done-text");
            text2.getStyleClass().add("todo-done-text");
            break;
        case 'D':
            text1.getStyleClass().add("deadline-done-text");
            text2.getStyleClass().add("deadline-done-text");
            break;
        case 'E':
            text1.getStyleClass().add("event-done-text");
            text2.getStyleClass().add("event-done-text");
            break;
        default:
            break;
        }
        return new Text[] {text1, doneMarkText, text2};
    }

    private Text styleUndoneTask(String undoneTask) {
        Text text = new Text(undoneTask);
        switch (undoneTask.charAt(Task.TYPE_POSITION)) {
        case 'T':
            text.getStyleClass().add("todo-text");
            break;
        case 'D':
            text.getStyleClass().add("deadline-text");
            break;
        case 'E':
            text.getStyleClass().add("event-text");
            break;
        default:
            break;
        }
        return text;
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    public static DialogBox getUserDialog(String message, Image img) {
        return new DialogBox(message, img);
    }

    public static DialogBox getHerm35Dialog(String[] message, Image img) {
        var db = new DialogBox(message, img);
        db.flip();
        return db;
    }
}
