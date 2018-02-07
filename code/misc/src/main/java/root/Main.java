package root;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.App;

public class Main {
  public static void main(String[] args) throws IOException {
    App app = new App();
    app.run(args);
  }

  public static Injector getInjector() {
    return Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));

        try {
          Config config = mapper.readValue(Main.class.getResource("/default.yaml"), Config.class);

          bind(Config.class).toInstance(config);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
