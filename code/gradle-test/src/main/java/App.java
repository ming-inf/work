import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
	ResourceBundle appBundle = ResourceBundle.getBundle("AppBundle", Locale.CANADA);

	public String getString(RESOURCE resourceKey) {
		return appBundle.getString(resourceKey.toString());
	}

	public String getGreeting() {
		return getString(RESOURCE.GREETING);
	}

	public void run(String[] args) throws IOException {
		if (!GraphicsEnvironment.isHeadless() && !Boolean.getBoolean("headless")) {
			launch(args);
		} else if (null != System.console()) {
			char[] pass = System.console().readPassword(getString(RESOURCE.PASSWORD_PROMPT));
			for (char c : pass) {
				System.out.println(c);
			}
		}
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SafePasswordField passwordField = new SafePasswordField();
		passwordField.setPromptText(getString(RESOURCE.PASSWORD_LABEL));
		passwordField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					System.out.println(passwordField.getPassword());
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Pane root = new StackPane();
		root.getChildren().add(passwordField);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
		root.requestFocus();
	}
}
