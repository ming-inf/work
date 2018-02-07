package javafx;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Injector;
import com.ibm.icu.util.ULocale;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.subjects.BehaviorSubject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.custom.SafePasswordField;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import root.Config;
import root.Main;

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

  ULocale confLocale;
  String confStylesheet;

  BehaviorSubject<ULocale> currentLocale;
  BehaviorSubject<ResourceBundle> appBundle;

  ObjectProperty<String> currentStylesheet = new SimpleObjectProperty<>();

  SafePasswordField password;
  ChoiceBox<ULocale> locales;
  ChoiceBox<String> theme;

  Scene scene;

  Injector injector = Main.getInjector();

  public App() {
    Config config = injector.getInstance(Config.class);
    confLocale = new ULocale(config.getLocale());
    confStylesheet = config.getStylesheet();

    appBundle = BehaviorSubject.create();

    currentLocale = BehaviorSubject.createDefault(confLocale);
    currentLocale.subscribe(newValue -> {
      appBundle.onNext(ResourceBundle.getBundle("AppBundle", newValue.toLocale()));
    });
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

    currentStylesheet.addListener((ob, o, n) -> {
      ObservableList<String> css = scene.getStylesheets();
      css.remove(o);
      css.add(n);
    });

    password = createPassword();
    locales = createLocales(localeList);
    theme = createTheme(styles);
  }

  private CheckBox hidePassword;
  private TextField textField;

  private SafePasswordField createPassword() {
    // text field to show password as unmasked
    textField = new TextField();
    // Set initial state
    textField.setManaged(false);
    textField.setVisible(false);

    hidePassword = new CheckBox("Show/Hide password");

    SafePasswordField passwordField = new SafePasswordField();

    // Bind properties. Toggle textField and passwordField
    // visibility and managability properties mutually when checkbox's state is changed.
    // Because we want to display only one component (textField or passwordField)
    // on the scene at a time.
    textField.managedProperty().bind(hidePassword.selectedProperty());
    textField.visibleProperty().bind(hidePassword.selectedProperty());

    passwordField.managedProperty().bind(hidePassword.selectedProperty().not());
    passwordField.visibleProperty().bind(hidePassword.selectedProperty().not());

    // Bind the textField and passwordField text values bidirectionally.
    textField.textProperty().bindBidirectional(passwordField.textProperty());

    appBundle.subscribe(newValue -> {
      passwordField.setPromptText(getString(RESOURCE.PASSWORD_LABEL));
    });
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
    JavaFxObservable.valuesOf(localesDropdown.valueProperty()).subscribe(newValue -> currentLocale.onNext(newValue));
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

    locales.getSelectionModel().select(confLocale);
    theme.getSelectionModel().select(confStylesheet);

    pane.requestFocus();

    log.info("application started");
  }

  private Pane createPane() {
    Pane pane = new FlowPane();
    pane.getChildren().add(password);
    pane.getChildren().add(textField);
    pane.getChildren().add(hidePassword);
    pane.getChildren().add(locales);
    pane.getChildren().add(theme);
    pane.getChildren().add(new Label("is windows: " + util.Platform.isWindows()));
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
