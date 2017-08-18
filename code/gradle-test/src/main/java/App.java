import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
	ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<Locale>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<ResourceBundle>();

	public App() {
		super();

		currentLocale.addListener((observable, oldValue, newValue) -> {
			appBundle.set(ResourceBundle.getBundle("AppBundle", newValue));
		});
		currentLocale.set(Locale.ROOT);
	}

	public String getString(RESOURCE resourceKey) {
		return appBundle.getValue().getString(resourceKey.toString());
	}

	public Callable<String> getCallableString(RESOURCE resourceKey) {
		return () -> getString(resourceKey);
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
		Button toggleLocale = new Button(getString(RESOURCE.ROOT_LABEL));
		toggleLocale.setOnAction(event -> {
			Locale newLocale = Locale.ROOT == currentLocale.get() ? Locale.CANADA : Locale.ROOT;
			currentLocale.setValue(newLocale);
		});

		SafePasswordField passwordField = new SafePasswordField();
		passwordField.promptTextProperty().bind(Bindings.createStringBinding(getCallableString(RESOURCE.PASSWORD_LABEL), appBundle));
		passwordField.setOnAction(event -> {
			try {
				System.out.println(passwordField.getPassword());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Pane root = new FlowPane();
		root.getChildren().add(passwordField);
		root.getChildren().add(toggleLocale);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		root.requestFocus();
	}
}
