import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
	ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<Locale>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<ResourceBundle>();

	public App() {
		super();

		currentLocale.addListener(new ChangeListener<Locale>() {
			@Override
			public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
				appBundle.set(ResourceBundle.getBundle("AppBundle", newValue));
			}
		});

		currentLocale.set(Locale.ROOT);
	}

	public String getString(RESOURCE resourceKey) {
		return appBundle.getValue().getString(resourceKey.toString());
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
		toggleLocale.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Locale l = currentLocale.get();
				currentLocale.setValue(Locale.ROOT == l ? Locale.CANADA : Locale.ROOT);
			}
		});

		SafePasswordField passwordField = new SafePasswordField();
		passwordField.setPromptText(getString(RESOURCE.PASSWORD_LABEL));
		currentLocale.addListener(new ChangeListener<Locale>() {
			@Override
			public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
				passwordField.setPromptText(getString(RESOURCE.PASSWORD_LABEL));
			}
		});
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
		root.getChildren().add(toggleLocale);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
		root.requestFocus();
	}
}
