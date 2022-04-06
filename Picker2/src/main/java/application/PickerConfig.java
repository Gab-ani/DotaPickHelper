package application;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;

@Configuration
public class PickerConfig {

	@Bean
	public Model model() {
		return new Model();
	}
	
	@Bean
	public Controller controller() {
		return new Controller();
	}
	
	@Bean
	public DraftWindow draftWindow() throws IOException {
		return new DraftWindow();
	}
	
	@Bean
	public PickerDAO pickerDAO() throws SQLException {
		return new PickerDAO();
	}
	
	@Bean
	public DotaPickHelperApplication app() {
		return new DotaPickHelperApplication();
	}
	
}
