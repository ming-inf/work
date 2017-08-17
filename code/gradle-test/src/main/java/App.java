import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App extends Application {
	ResourceBundle appBundle = ResourceBundle.getBundle("AppBundle", Locale.CANADA);

	public String getGreeting() {
		return appBundle.getString("greeting");
	}

	public void run(String[] args) throws IOException {
		if (!GraphicsEnvironment.isHeadless() && !Boolean.getBoolean("headless")) {
			launch(args);
		} else if (null != System.console()) {
			char[] pass = System.console().readPassword(appBundle.getString("passwordPrompt"));
			for (char c : pass) {
				System.out.println(c);
			}
		}
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SafePasswordField passwordField = new SafePasswordField();
		passwordField.setPromptText(appBundle.getString("passwordLabel"));
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
	}

	public class SafePasswordField extends PasswordField {
		public final char[] getPassword()
				throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			Content c = getContent();

			Field fld = c.getClass().getDeclaredField("characters");
			fld.setAccessible(true);

			StringBuilder sb = (StringBuilder) fld.get(c);
			char[] result = new char[sb.length()];
			sb.getChars(0, sb.length(), result, 0);

			return result;
		}
	}
}
