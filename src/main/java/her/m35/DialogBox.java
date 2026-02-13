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

    private DialogBox(String message, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getChildren().add(new Text(message));
        dialog.getStyleClass().add("dialog-box");
        displayPicture.setImage(img);
    }

    private DialogBox(String[] message, Image img) {
        System.out.println(message);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Form dialog text with coloured highlighting based on content of text
        for (String segment : message) {
            if (segment.startsWith("[")) {
                boolean taskDone = String.valueOf(segment.charAt(Task.MARK_POSITION)).equals(Task.DONE_MARK);
                if (taskDone) {
                    Text text1 = new Text(segment.substring(0, Task.MARK_POSITION));
                    Text doneMarkText = new Text(Task.DONE_MARK);
                    doneMarkText.getStyleClass().add("done-mark");
                    Text text2 = new Text(segment.substring(Task.MARK_POSITION + 1));
                    switch (segment.charAt(Task.TYPE_POSITION)) {
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
                    dialog.getChildren().add(text1);
                    dialog.getChildren().add(doneMarkText);
                    dialog.getChildren().add(text2);
                } else {
                    Text text = new Text(segment);
                    switch (segment.charAt(Task.TYPE_POSITION)) {
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
                    dialog.getChildren().add(text);
                }
            } else if (segment.startsWith("#")) {
                String[] tags = segment.split("(?=#)|(?=,)");
                for (String tag : tags) {
                    Text tagText = new Text(tag);
                    if (tag.startsWith("#")) {
                        tagText.getStyleClass().add("tag-text");
                    }
                    dialog.getChildren().add(tagText);
                }
            } else {
                dialog.getChildren().add(new Text(segment));
            }
        }
        dialog.getStyleClass().add("dialog-box");
        displayPicture.setImage(img);
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
