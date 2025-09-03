package duke.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import duke.MrMoon;

/**
 * A GUI for MrMoon using FXML.
 */
public class Main extends Application {
    private MrMoon mrMoon = new MrMoon("./data/duke.txt");

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("MrMoon Chatbot");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            fxmlLoader.<MainWindow>getController().setMrMoon(mrMoon);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
