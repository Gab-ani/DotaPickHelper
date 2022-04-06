package application;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;

@Configuration
public class PickerConfig {

	@Bean
	public Model model() {
		Model m = new Model();
		return m;
	}
	
	@Bean
	public Controller controller() {
		Controller c = new Controller();
		return c;
	}
	
	@Bean
	public DraftWindow draftWindow() throws IOException {
		DraftWindow w = new DraftWindow();
		return w;
	}
	
	@Bean
	public DotaPickHelperApplication app() {
		return new DotaPickHelperApplication();
	}
}
