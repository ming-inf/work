import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import javafx.App;

public class Main {
	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));

		Config config = mapper.readValue(Main.class.getResource("config.yaml"), Config.class);
		mapper.writeValue(System.out, config);

		App app = new App();
		app.run(args);
	}
}
