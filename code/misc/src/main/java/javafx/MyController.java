package javafx;

import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.icu.util.ULocale;
import com.sun.javafx.PlatformUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SafePasswordField;
import javafx.util.StringConverter;

public class MyController {
	static final Logger log = LogManager.getLogger(MyController.class);

	@FXML
	private Button button;

	@FXML
	private SafePasswordField passwordField;

	@FXML
	private Label isWindows;

	@FXML
	private ResourceBundle resources;

	@FXML
	private ChoiceBox<ULocale> locales;

	ObservableList<ULocale> localeList = FXCollections.observableArrayList(ULocale.ROOT, ULocale.CANADA);

	ObjectProperty<ULocale> currentLocale = new SimpleObjectProperty<>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<>();

	@FXML
	protected void initialize() {
		appBundle.set(resources);
		button.setOnAction(event -> {
			System.out.println("You clicked me!");
		});

		passwordField.promptTextProperty().bind(Bindings.createStringBinding(getCallableString(RESOURCE.PASSWORD_LABEL), appBundle));
		passwordField.setOnAction(event -> {
			try {
				System.out.println(passwordField.getPassword());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				log.catching(e);
			}
		});
		createLocales(localeList);
		locales.getSelectionModel().select(ULocale.ROOT);
		labelText();
	}

	private void createLocales(ObservableList<ULocale> localeList) {
		locales.setItems(localeList);
		locales.setConverter(new LocaleConverter());
		locales.valueProperty().addListener((observable, oldValue, newValue) -> currentLocale.set(newValue));
	}

	public void labelText() {
		isWindows.setText("is windows: " + PlatformUtil.isWindows());
	}

	public String getString(RESOURCE resourceKey) {
		return appBundle.getValue().getString(resourceKey.toString());
	}

	public Callable<String> getCallableString(RESOURCE resourceKey) {
		return () -> getString(resourceKey);
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
