import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow {

	final static int windowWidth = 1500;
	final static int windowHeight = 600;   			// global window
	
	final static int heroIconWidth = 128;
	final static int heroIconHeight = 72;			// pick icons
	final static int heroIconSpace = 12;		
	final static int heroIconStartSpaceX = 20;
	final static int heroIconStartSpaceY = 30;
	
	final static int candIconWidth = 256;			// central icon
	final static int candIconHeight = 144;
	
	final static Rectangle textRect = new Rectangle(windowWidth / 2 - 145, windowHeight / 2 + 75, 290, 100);
	
	final static Rectangle exitRectangle = new Rectangle(windowWidth / 2 - 155, 485, 70, 70);
	final static Rectangle settingsRectangle = new Rectangle(windowWidth / 2 - 75, 485, 70, 70);
	final static Rectangle infoRectangle = new Rectangle(windowWidth / 2 + 5, 485, 70, 70);
	final static Rectangle resetRectangle = new Rectangle(windowWidth / 2 + 85, 485, 70, 70);
	
	final static int suggestionWidth = 100;
	final static int suggestionHeight = 50;
	final static int suggestionSpace = 6;
	final static int radiantSuggestionsStartX = 15;
	final static int radiantSuggestionsStartY = 250;
	final static int direSuggestionsStartX = 950;
	final static int direSuggestionsStartY = 250;
	
	final static int roleIconWidth = 30;
	final static int roleIconHeight = 30;
	final static int radiantRoleIconStartX = 65;
	final static int radiantRoleIconStartY = 210;
	final static int direRoleIconStartX = 1000;
	final static int direRoleIconStartY = 210;
	
	
	public View fatherView;
	
	private JFrame window;
	
	private JLabel[] pickIcons;
	
	private JLabel[] roleIconsRadiant;
	private JLabel[] roleIconsDire;
	private JLabel[][] suggestionsRadiant;		
	private JLabel[][] suggestionsDire;
	
	private JButton candidateLabel;
	
	private String input;
	private JLabel inputLabel;
	
	private JLabel nextHeroSlot;
	
//	private JLabel supportLabelRadiant;
//	private JLabel coreLabelRadiant;
//	private JLabel supportLabelDire;
//	private JLabel coreLabelDire;
	
	private JButton resetButton;
	private JButton settingsButton;
	private JButton exitButton;
	private JButton infoButton;
	
	public JButton getResetButton() {
		return resetButton;
	}
	
	public JButton getSettingsButton() {
		return settingsButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
	
	public JButton getInfoButton() {
		return infoButton;
	}
	
	public void updateNextSlotLabel(int index) {
		nextHeroSlot.setLocation(pickIcons[index].getLocation().x + heroIconWidth / 2 - nextHeroSlot.getWidth() / 2, pickIcons[index].getLocation().y + heroIconHeight + 10);
	}
	
	public void exit() {
		System.exit(0);
	}
	
	public void showSettingsWindow() {
		SettingsWindow settings = new SettingsWindow();
		settings.setupWindow();
		getCandidateLabel().requestFocus();
	}
	
	public void showInfo() {
		
	}
	
	public void updatePick() throws IOException {
		for(int i = 0; i < 10; i++) {
			pickIcons[i].setIcon(fatherView.model.getPick()[i].getIcon());
		}
	}
	
	public void resetSuggestions() throws IOException {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				View.setIconByHero(suggestionsDire[i][j], "unknown", 128, 72);
			}
		}
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				View.setIconByHero(suggestionsRadiant[i][j], "unknown", 128, 72);
			}
		}
	}
	
	public void updateSuggestions() throws IOException {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				View.setIconByHero(suggestionsDire[i][j], fatherView.model.direSuggestionPool.get(i).get(j).getName(), 128, 72);
			}
		}
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				View.setIconByHero(suggestionsRadiant[i][j], fatherView.model.radiantSuggestionPool.get(i).get(j).getName(), 128, 72);
			}
		}
	}
	
	public void updateInputLabel() {
		inputLabel.setText(input);
	}
	
	public void updateCandidateName() throws IOException {
		View.setIconByHero(getCandidateLabel(), fatherView.model.getCandidate().getName(), 256, 144);
	}
	
	public String getInput() {
		return input;
	}
	
	public String getCandidateText() {
		return candidateLabel.getText();
	}
		
	public JButton getCandidateLabel() {
		return candidateLabel;
	}
	
	public JLabel[] getIcons() {
		return pickIcons;
	}
	
	public void setView(View v) {
		fatherView = v;
	}
	
	public void setInput(String s) {
		input = s;
		updateInputLabel();
	}
	
	public void setupSuggestionLabels() throws IOException {
		suggestionsDire = new JLabel[5][5];
		roleIconsDire = new JLabel[5];
		for(int i = 0; i < 5; i++) {
			roleIconsDire[i] = new JLabel(i + 1 + "");
			roleIconsDire[i].setBounds(new Rectangle(direRoleIconStartX + i * (suggestionWidth + suggestionSpace), direRoleIconStartY, roleIconWidth, roleIconHeight));
			window.add(roleIconsDire[i]);
			for(int j = 0; j < 5; j++) {
				suggestionsDire[i][j] = new JLabel();
				View.setIconByHero(suggestionsDire[i][j], "unknown", suggestionWidth, suggestionHeight);
				suggestionsDire[i][j].setBounds(new Rectangle(direSuggestionsStartX + i * (suggestionWidth + suggestionSpace), 
															  direSuggestionsStartY + j * (suggestionHeight + suggestionSpace), 
															  suggestionWidth,
															  suggestionHeight));
				window.add(suggestionsDire[i][j]);
			}
		}
		suggestionsRadiant = new JLabel[5][5];
		roleIconsRadiant = new JLabel[5];
		for(int i = 0; i < 5; i++) {
			roleIconsRadiant[i] = new JLabel(i + 1 + "");
			roleIconsRadiant[i].setBounds(new Rectangle(radiantRoleIconStartX + i * (suggestionWidth + suggestionSpace), radiantRoleIconStartY, roleIconWidth, roleIconHeight));
			window.add(roleIconsRadiant[i]);
			for(int j = 0; j < 5; j++) {
				suggestionsRadiant[i][j] = new JLabel();
				View.setIconByHero(suggestionsRadiant[i][j], "unknown", suggestionWidth, suggestionHeight);
				suggestionsRadiant[i][j].setBounds(new Rectangle(radiantSuggestionsStartX + i * (suggestionWidth + suggestionSpace), 
															  	 radiantSuggestionsStartY + j * (suggestionHeight + suggestionSpace), 
															  	 suggestionWidth,
															  	 suggestionHeight));
				window.add(suggestionsRadiant[i][j]);
			}
		}
	}
	
	public void setupWindow() throws IOException {
		
		window = new JFrame("Dota2Picker");
		window.setBounds(new Rectangle(0, 0, windowWidth, windowHeight));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setResizable(false);
//		window.setUndecorated(true);
		
		pickIcons = new JLabel[10];
		
		for(int i = 0; i < 5; i++) {		// left row
			pickIcons[i] = new JLabel();
			pickIcons[i].setBounds(new Rectangle(heroIconStartSpaceX + i * (heroIconWidth + heroIconSpace), heroIconStartSpaceY, heroIconWidth, heroIconHeight));
			window.add(pickIcons[i]);
		}		
		
		for(int i = 9; i > 4; i--) {		// right row
			pickIcons[i] = new JLabel("r" + i);
			pickIcons[i].setBounds(new Rectangle(window.getWidth() - heroIconStartSpaceX - (10-i) * (heroIconWidth + heroIconSpace), heroIconStartSpaceY, heroIconWidth, heroIconHeight));
			window.add(pickIcons[i]);
		}
		
		candidateLabel = new JButton();
		candidateLabel.setBounds(new Rectangle(window.getWidth() / 2 - candIconWidth / 2, window.getHeight() / 2 - candIconHeight / 2 - 30, candIconWidth, candIconHeight));
		window.add(candidateLabel);
		
		inputLabel = new JLabel();
		inputLabel.setBounds(textRect);
		inputLabel.setBorder(BorderFactory.createBevelBorder(1));
		inputLabel.setVerticalAlignment(JLabel.CENTER);
		inputLabel.setHorizontalAlignment(JLabel.CENTER);
		window.add(inputLabel);
				
		nextHeroSlot = new JLabel();
		nextHeroSlot.setIcon(new ImageIcon("resources/icons/UpSide1.png"));
		nextHeroSlot.setBounds(new Rectangle(100, 100, 60, 70));
		updateNextSlotLabel(0);
		window.add(nextHeroSlot);
		
		// TODO make this shit in rectangles
		
		setupSuggestionLabels();
		
		exitButton = new JButton("exit");
		exitButton.setBounds(exitRectangle);
		window.add(exitButton);
		
		settingsButton = new JButton("settings");
		settingsButton.setBounds(settingsRectangle);
		settingsButton.setVisible(false);
		window.add(settingsButton);
		
		infoButton = new JButton("info");
		infoButton.setBounds(infoRectangle);
		window.add(infoButton);
		
		resetButton = new JButton("reset");
		resetButton.setBounds(resetRectangle);
		window.add(resetButton);
		
		
		input = "";
		updateCandidateName();
		updatePick();
		
		window.setVisible(true);
	}
	
}
