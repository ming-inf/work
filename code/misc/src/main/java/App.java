import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.ibm.icu.util.ULocale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SafePasswordField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class App extends Application {
	ListProperty<ULocale> localeList = new SimpleListProperty<>();
	ObjectProperty<ULocale> currentLocale = new SimpleObjectProperty<>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<>();

	public App() {
		super();

		localeList.set(FXCollections.observableArrayList(ULocale.ROOT, ULocale.CANADA));

		currentLocale.addListener((observable, oldValue, newValue) -> {
			appBundle.set(ResourceBundle.getBundle("AppBundle", newValue.toLocale()));
		});
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
		passwordField.promptTextProperty().bind(Bindings.createStringBinding(getCallableString(RESOURCE.PASSWORD_LABEL), appBundle));
		passwordField.setOnAction(event -> {
			try {
				System.out.println(passwordField.getPassword());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		ComboBox<ULocale> localesDropdown = new ComboBox<>();
		Callback<ListView<ULocale>, ListCell<ULocale>> cellFactory = new Callback<ListView<ULocale>, ListCell<ULocale>>() {
			@Override
			public ListCell<ULocale> call(ListView<ULocale> param) {
				return new LocalesFormatCell();
			}
		};
		localesDropdown.setButtonCell(cellFactory.call(null));
		localesDropdown.setCellFactory(cellFactory);
		localesDropdown.setItems(localeList);
		localesDropdown.getSelectionModel().selectFirst();
		currentLocale.bind(localesDropdown.getSelectionModel().selectedItemProperty());

		Pane root = new FlowPane();
		root.getChildren().add(passwordField);
		root.getChildren().add(localesDropdown);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		root.requestFocus();
	}

	public void printAvailableLocales() {
		Set<String> locales = Arrays.stream(ULocale.getAvailableLocales())
			.filter(l -> {return !l.getCountry().isEmpty();})
			.map(l -> {return String.format("%s: %s, %s; %s, %s", l, l.getDisplayLanguage(), l.getDisplayCountry(), l.getDisplayLanguage(l), l.getDisplayCountry(l));})
			.collect(Collectors.toCollection(TreeSet::new));
		locales.forEach(System.out::println);
	}
}

class LocalesFormatCell extends ListCell<ULocale> {
	@Override
	protected void updateItem(ULocale item, boolean empty) {
		super.updateItem(item, empty);

		if (ULocale.ROOT == item) {
			setText("default");
		} else if (null != item) {
			setText(item.toString());
		}
	}
}
