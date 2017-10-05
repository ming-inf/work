package javafx;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.ibm.icu.util.ULocale;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SafePasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class App extends Application {
	ObjectProperty<ULocale> currentLocale = new SimpleObjectProperty<>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<>();

	SafePasswordField passwordField;
	ComboBox<ULocale> localesDropdown;
	ComboBox<String> stylesheetDropdown;

	ObservableList<ULocale> localeList = FXCollections.observableArrayList(ULocale.ROOT, ULocale.CANADA);
	ObservableList<String> styles = FXCollections.observableArrayList("default.css", "light.css", "dark.css");

	Callback<ListView<ULocale>, ListCell<ULocale>> cellFactory = param -> new LocalesFormatCell();

	public App() {
		super();

		currentLocale.addListener((observable, oldValue, newValue) -> appBundle.set(ResourceBundle.getBundle("AppBundle", newValue.toLocale())));
		currentLocale.set(ULocale.ROOT);
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

		Application.setUserAgentStylesheet(STYLESHEET_MODENA);

		passwordField = new SafePasswordField();
		passwordField.promptTextProperty().bind(Bindings.createStringBinding(getCallableString(RESOURCE.PASSWORD_LABEL), appBundle));
		passwordField.setOnAction(event -> {
			try {
				System.out.println(passwordField.getPassword());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		localesDropdown = new ComboBox<>(localeList);
		localesDropdown.setButtonCell(cellFactory.call(null));
		localesDropdown.setCellFactory(cellFactory);
		localesDropdown.valueProperty().addListener((observable, oldValue, newValue) -> currentLocale.set(newValue));

		stylesheetDropdown = new ComboBox<>(styles);
		stylesheetDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<String> css = stylesheetDropdown.getScene().getStylesheets();
			css.remove(oldValue);
			css.add(newValue);
		});
	}

	class StylesheetFormatCell extends ListCell<String> {
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (null == item || empty) {
				setText(null);
				setGraphic(null);
				return;
			}

			setText(item.toString());
			setOnMouseEntered(event -> {
				ObservableList<String> css = stylesheetDropdown.getScene().getStylesheets();
				css.clear();
				css.add(item);
			});
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = new FlowPane();
		root.getChildren().add(passwordField);
		root.getChildren().add(localesDropdown);
		root.getChildren().add(stylesheetDropdown);
		root.getChildren().add(new Label("is windows: " + PlatformUtil.isWindows()));

		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		localesDropdown.getSelectionModel().selectFirst();
		stylesheetDropdown.getSelectionModel().selectFirst();

		root.requestFocus();

		ListView<String> lv = ((ComboBoxListViewSkin<String>) stylesheetDropdown.getSkin()).getListView();
		lv.setCellFactory(listView -> new StylesheetFormatCell());
		lv.setOnMouseExited(event -> {
			ObservableList<String> css = stylesheetDropdown.getScene().getStylesheets();
			css.clear();
			css.add(stylesheetDropdown.getSelectionModel().getSelectedItem());
		});
	}

	public void printAvailableLocales() {
		Set<String> locales = Arrays
				.stream(ULocale.getAvailableLocales())
				.filter(l -> !l.getCountry().isEmpty())
				.map(l -> String.format("%s: %s, %s; %s, %s", l, l.getDisplayLanguage(), l.getDisplayCountry(), l.getDisplayLanguage(l),
						l.getDisplayCountry(l)))
				.collect(Collectors.toCollection(TreeSet::new));
		locales.forEach(System.out::println);
	}

	public void land() {
		Platform.exit();
	}
}

class LocalesFormatCell extends ListCell<ULocale> {
	@Override
	protected void updateItem(ULocale item, boolean empty) {
		super.updateItem(item, empty);

		if (null == item || empty) {
			setText(null);
			setGraphic(null);
		} else if (ULocale.ROOT == item) {
			setText("default");
		} else if (null != item) {
			setText(item.toString());
		}
	}
}
