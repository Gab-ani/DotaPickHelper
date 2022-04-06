package application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DotaPickHelperApplication {
	
	@Autowired
	Model model;
	@Autowired
	Controller controller;
	@Autowired
	DraftWindow draftWindow;

	public static void main(String[] args) throws IOException {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(PickerConfig.class);
		
		DotaPickHelperApplication app = context.getBean(DotaPickHelperApplication.class);
		app.startUp();
//		app.run();
		
		((AnnotationConfigApplicationContext) context).close();
	}

	private void startUp() throws IOException {
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		draftWindow.setupWindow();
		controller.init();
	}
//	
//	private void run() {
//		
//	}

	public void reset() {
		// TODO
	}
	
}
