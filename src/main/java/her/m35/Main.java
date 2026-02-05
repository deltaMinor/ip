package her.m35;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for HERM35 using FXML.
 */
public class Main extends Application {

    private Herm35 herm35 = new Herm35();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            stage.setMinHeight(320);
            stage.setMinWidth(417);

            fxmlLoader.<MainWindow>getController().setHerm35(herm35);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
