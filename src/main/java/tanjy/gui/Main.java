package tanjy.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tanjy.Tanjy;

/**
 * The entry point for the GUI application.
 */
public class Main extends Application {

    private final Tanjy tanjy = new Tanjy("./data/tanjy.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Gui.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            fxmlLoader.<Gui>getController().setTanjy(tanjy);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
