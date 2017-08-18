import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

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
	ListProperty<Locale> localeList = new SimpleListProperty<Locale>();
	ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<Locale>();
	ObjectProperty<ResourceBundle> appBundle = new SimpleObjectProperty<ResourceBundle>();

	public App() {
		super();

		localeList.set(FXCollections.observableArrayList(Locale.ROOT, Locale.CANADA));

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

		ComboBox<Locale> localesDropdown = new ComboBox<Locale>();
		Callback<ListView<Locale>, ListCell<Locale>> cellFactory = new Callback<ListView<Locale>, ListCell<Locale>>() {
			@Override
			public ListCell<Locale> call(ListView<Locale> param) {
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
}

class LocalesFormatCell extends ListCell<Locale> {
	@Override
	protected void updateItem(Locale item, boolean empty) {
		super.updateItem(item, empty);

		if (Locale.ROOT == item) {
			setText("default");
		} else if (null != item) {
			setText(item.toString());
		}
	}
}
