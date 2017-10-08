package javafx;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.icu.util.ULocale;
import com.sun.javafx.PlatformUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class App extends Application {
	static {
		try {
			InputStream is = App.class.getResourceAsStream("/logging.properties");
			java.util.logging.LogManager.getLogManager().readConfiguration(is);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	static final Logger log = LogManager.getLogger(App.class);

	ObservableList<ULocale> localeList = FXCollections.observableArrayList(ULocale.ROOT, ULocale.CANADA);
	ObservableList<String> styles = FXCollections.observableArrayList("default.css", "light.css", "dark.css");

	ULocale defaultULocale = ULocale.ROOT;
	ObjectProperty<ULocale> currentLocale = new SimpleObjectProperty<>(defaultULocale);
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<>(ResourceBundle.getBundle("AppBundle", defaultULocale.toLocale()));

	SafePasswordField passwordField;
	ChoiceBox<ULocale> localesDropdown;
	ChoiceBox<String> stylesheetDropdown;

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
			char[] pass = System.console().readPassword(getString(RESOURCE.PASSWORD_PROMPT) + " ");
			for (char c : pass) {
				System.out.println(c);
			}
		}
		land();
	}

	@Override
	public void init() throws Exception {
		super.init();

		currentLocale.addListener((observable, oldValue, newValue) -> appBundle.set(ResourceBundle.getBundle("AppBundle", newValue.toLocale())));

		passwordField = createPasswordField();
		localesDropdown = createLocalesDropdown(localeList);
		stylesheetDropdown = createStylesheetDropdown(styles);
	}

	private SafePasswordField createPasswordField() {
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
		return passwordField;
	}

	private ChoiceBox<ULocale> createLocalesDropdown(ObservableList<ULocale> localeList) {
		ChoiceBox<ULocale> localesDropdown = new ChoiceBox<>(localeList);
		localesDropdown.setConverter(new LocaleConverter());
		localesDropdown.valueProperty().addListener((observable, oldValue, newValue) -> currentLocale.set(newValue));
		return localesDropdown;
	}

	private ChoiceBox<String> createStylesheetDropdown(ObservableList<String> styles) {
		ChoiceBox<String> stylesheetDropdown = new ChoiceBox<>(styles);
		stylesheetDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<String> css = stylesheetDropdown.getScene().getStylesheets();
			css.remove(oldValue);
			css.add(newValue);
		});
		return stylesheetDropdown;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = createPane();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();

		localesDropdown.getSelectionModel().selectFirst();
		stylesheetDropdown.getSelectionModel().selectFirst();

		pane.requestFocus();

		log.info("application started");
	}

	private Pane createPane() {
		Pane pane = new FlowPane();
		pane.getChildren().add(passwordField);
		pane.getChildren().add(localesDropdown);
		pane.getChildren().add(stylesheetDropdown);
		pane.getChildren().add(new Label("is windows: " + PlatformUtil.isWindows()));
		return pane;
	}

	public void land() {
		Platform.exit();
	}

	class LocaleConverter extends StringConverter<ULocale> {
		@Override
		public String toString(ULocale object) {
			return ULocale.ROOT == object ? "default" : object.toString();
		}

		@Override
		public ULocale fromString(String string) {
			return null;
		}
	}
}
