package application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PickerConfig.class);
		
		DotaPickHelperApplication app = context.getBean(DotaPickHelperApplication.class);
		app.startUp();
		
		context.close();
	}

	private void startUp() throws IOException {
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		draftWindow.setupWindow();
		controller.init();
		
//		model.pickerDAO.createAdvantageTables("");
	}

	public void reset() throws IOException {
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		draftWindow.createTotalAdvantageArrays();
		draftWindow.updateCandidateName();
		draftWindow.updatePick();
		draftWindow.resetSuggestions();
		draftWindow.updateNextSlotLabel(0);
		draftWindow.getCandidateLabel().requestFocus();
	}
	
}
