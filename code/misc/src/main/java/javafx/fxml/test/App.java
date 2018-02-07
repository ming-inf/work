package javafx.fxml.test;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    URL location = getClass().getResource("example.fxml");
    ResourceBundle resources = ResourceBundle.getBundle("AppBundle");
    FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

    Parent root = (Pane) fxmlLoader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String... args) {
    launch(args);
  }
}
