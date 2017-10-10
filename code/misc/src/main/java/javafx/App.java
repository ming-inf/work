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
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
	String defaultStylesheet = "default.css";
	ObjectProperty<ULocale> currentLocale = new SimpleObjectProperty<>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<>();
	ObjectProperty<String> currentStylesheet = new SimpleObjectProperty<>();

	SafePasswordField password;
	ChoiceBox<ULocale> locales;
	ChoiceBox<String> theme;

	Scene scene;

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
			land();
		} else if (null != System.console()) {
			char[] pass = System.console().readPassword(getString(RESOURCE.PASSWORD_PROMPT) + " ");
			for (char c : pass) {
				System.out.println(c);
			}
		}
	}

	@Override
	public void init() throws Exception {
		super.init();

		currentLocale.addListener((observable, oldValue, newValue) -> appBundle.set(ResourceBundle.getBundle("AppBundle", newValue.toLocale())));
		currentStylesheet.addListener((ob, o, n) -> {
			ObservableList<String> css = scene.getStylesheets();
			css.remove(o);
			css.add(n);
		});

		currentLocale.set(defaultULocale);

		password = createPassword();
		locales = createLocales(localeList);
		theme = createTheme(styles);
	}

	private SafePasswordField createPassword() {
		SafePasswordField passwordField = new SafePasswordField();
		passwordField.promptTextProperty().bind(Bindings.createStringBinding(getCallableString(RESOURCE.PASSWORD_LABEL), appBundle));
		passwordField.setOnAction(event -> {
			try {
				System.out.println(passwordField.getPassword());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				log.catching(e);
			}
		});
		return passwordField;
	}

	private ChoiceBox<ULocale> createLocales(ObservableList<ULocale> localeList) {
		ChoiceBox<ULocale> localesDropdown = new ChoiceBox<>(localeList);
		localesDropdown.setConverter(new LocaleConverter());
		localesDropdown.valueProperty().addListener((observable, oldValue, newValue) -> currentLocale.set(newValue));
		return localesDropdown;
	}

	private ChoiceBox<String> createTheme(ObservableList<String> styles) {
		ChoiceBox<String> stylesheetDropdown = new ChoiceBox<>(styles);
		stylesheetDropdown.valueProperty().addListener((observable, oldValue, newValue) -> currentStylesheet.set(newValue));
		return stylesheetDropdown;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = createPane();
		scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();

		locales.getSelectionModel().selectFirst();
		theme.getSelectionModel().selectFirst();

		pane.requestFocus();

		log.info("application started");
	}

	private Pane createPane() {
		Pane pane = new FlowPane();
		pane.getChildren().add(password);
		pane.getChildren().add(locales);
		pane.getChildren().add(theme);
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
