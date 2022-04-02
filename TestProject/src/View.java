import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class View {
	
	
	
	Controller controller;
	Model model;
	
	MainWindow mainWindow;
	
	public void setMainWindow(MainWindow m) {
		mainWindow = m;
	}

	public void setModel(Model m) {
		model = m;
	}
	
	public void setController(Controller c) {
		controller = c;
	}
	
	public void reset() throws IOException {
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		
		mainWindow.updateCandidateName();
		mainWindow.updatePick();
		mainWindow.resetSuggestions();
		mainWindow.updateNextSlotLabel(0);
		mainWindow.getCandidateLabel().requestFocus();
	}
	
	private void setupAll() throws IOException {
		mainWindow = new MainWindow();
		mainWindow.setView(this);
		
		
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		
		mainWindow.setupWindow();
		controller.init();
	}
	
	public static void setIconByHero(JButton button, String hero, int neededWidth, int neededHeight) throws IOException {
		button.setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + hero + ".png")).getScaledInstance(neededWidth, neededHeight, Image.SCALE_SMOOTH)));
	}
	
	public static void setIconByHero(JLabel label, String hero, int neededWidth, int neededHeight) throws IOException {
		label.setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + hero + ".png")).getScaledInstance(neededWidth, neededHeight, Image.SCALE_SMOOTH)));
	}
	
	private void run() throws IOException, ParseException {
		//model.direCores.forEach(hero -> System.out.println(hero.getAdvantage()));
		//SQLUtility.setRolesColumn();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException {
		
		Model model = new Model();
		View app = new View();
		Controller ctrl = new Controller();
		
		app.setController(ctrl);
		app.setModel(model);
		
		app.controller.setApp(app);
		app.controller.setModel(model);
		
		app.model.setApp(app);

		app.setupAll();
		app.run();
		
	}
}