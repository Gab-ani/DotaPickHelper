import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MainWindow {
	/////////////////////////////////////////////  + constants
	final static int windowWidth = 1500;
	final static int windowHeight = 600;   			// global window
	
	final static int heroIconWidth = 128;
	final static int heroIconHeight = 72;			// pick icons
	final static int heroIconSpace = 12;		
	final static int heroIconStartSpaceX = 20;
	final static int heroIconStartSpaceY = 30;
	
	final static int candIconWidth = 256;			// central icon
	final static int candIconHeight = 144;
	
	
	
	final static Rectangle textRect = new Rectangle(windowWidth / 2 + 240, windowHeight / 2 - 75, 290, 150);
	/////////////////////////////////////////////  - constants
	
	Controller controller;
	Model model;
	
	private JLabel[] pickIcons;
	
	private JLabel[] suggestedSupportIcons;
	private JLabel[] suggestedCoreIcons;
	
	private JButton candidateLabel;
	private String input;
	private JLabel inputLabel;
	private JLabel nextHeroSlot;
	private JLabel supportLabel;
	private JLabel coreLabel;
	
	public void updateNextSlotLabel(int index) {
		nextHeroSlot.setLocation(pickIcons[index].getLocation().x + heroIconWidth / 2 - nextHeroSlot.getWidth() / 2, pickIcons[index].getLocation().y + heroIconHeight + 10);
	}
	
	public void updatePick() throws IOException {
		for(int i = 0; i < 10; i++) {
			pickIcons[i].setIcon(model.getPick()[i].getIcon());
		}
	}
	
	public void updateInputLabel() {
		inputLabel.setText(input);
	}
	
	public void updateCandidateName() {
		//getCandidateLabel().setText(model.getCandidate().getName());
		ImageIcon icon = new ImageIcon("resources/icons/" + model.getCandidate().getName() + ".png");
		Image imageToRescale = icon.getImage().getScaledInstance(256, 144, Image.SCALE_SMOOTH);
		icon = new ImageIcon(imageToRescale);
		//ImageIcon i = model.getCandidate().getIcon();
		getCandidateLabel().setIcon(icon);
	}
	////////////////////////////////////////////////////////	 + GETTERS
	
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
	////////////////////////////////////////////////////////  	 - GETTERS
	////////////////////////////////////////////////////////  	 + SETTERS
	
	public void setInput(String s) {
		input = s;
		updateInputLabel();
	}
	
	public void setModel(Model m) {
		model = m;
	}
	
	public void setController(Controller c) {
		controller = c;
	}
	
	////////////////////////////////////////////////////////  	 - SETTERS
	private void init() throws IOException {
		
		JFrame window = new JFrame("Dota2Picker");
		window.setBounds(new Rectangle(0, 0, windowWidth, windowHeight));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setResizable(false);
		
		/////////////////////////////////////////// + hero icons
		pickIcons = new JLabel[10];
		
		for(int i = 0; i < 5; i++) {		// left row
			pickIcons[i] = new JLabel();
//			icons[i].setBorder(BorderFactory.createBevelBorder(1));
//			icons[i].setBackground(new Color(0, 255, 0));
			pickIcons[i].setBounds(new Rectangle(heroIconStartSpaceX + i * (heroIconWidth + heroIconSpace), heroIconStartSpaceY, heroIconWidth, heroIconHeight));
			window.add(pickIcons[i]);
		}		
		
		for(int i = 9; i > 4; i--) {		// right row
			pickIcons[i] = new JLabel("r" + i);
//			icons[i].setBorder(BorderFactory.createBevelBorder(1));
//			icons[i].setBackground(new Color(0, 255, 0));
			pickIcons[i].setBounds(new Rectangle(window.getWidth() - heroIconStartSpaceX - (10-i) * (heroIconWidth + heroIconSpace), heroIconStartSpaceY, heroIconWidth, heroIconHeight));
			window.add(pickIcons[i]);
		}
		/////////////////////////////////////////// - hero icons
		/////////////////////////////////////////// + candidate button
		
		candidateLabel = new JButton();
		candidateLabel.setBounds(new Rectangle(window.getWidth() / 2 - candIconWidth / 2, window.getHeight() / 2 - candIconHeight / 2, candIconWidth, candIconHeight));
		
		window.add(candidateLabel);
		
		/////////////////////////////////////////// - candidate button
		/////////////////////////////////////////// + candidate text
		
		inputLabel = new JLabel();
		inputLabel.setBounds(textRect);
		inputLabel.setBorder(BorderFactory.createBevelBorder(1));
		inputLabel.setVerticalAlignment(JLabel.CENTER);
		inputLabel.setHorizontalAlignment(JLabel.CENTER);
		window.add(inputLabel);
		
		/////////////////////////////////////////// - candidate text
		/////////////////////////////////////////// + nextHeroSlot
				
		nextHeroSlot = new JLabel();
		nextHeroSlot.setIcon(new ImageIcon("icons/UpSide1.png"));
		nextHeroSlot.setBounds(new Rectangle(100, 100, 60, 70));
		updateNextSlotLabel(0);
		window.add(nextHeroSlot);
		
		/////////////////////////////////////////// - nextHeroSlot
		/////////////////////////////////////////// + supportLabels
				
		supportLabel = new JLabel("сап");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		supportLabel.setBorder(BorderFactory.createBevelBorder(1));
		supportLabel.setBounds(new Rectangle(250, 200, 60, 30));
		window.add(supportLabel);
		
		suggestedSupportIcons = new JLabel[5];
		for(int i = 0; i < suggestedSupportIcons.length; i++) {
			suggestedSupportIcons[i] = new JLabel("" + i);
			suggestedSupportIcons[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedSupportIcons[i].setBounds(new Rectangle(230, 190 + (i+1)*55, 100, 50));
			window.add(suggestedSupportIcons[i]);
		}
		
		
		/////////////////////////////////////////// - supportLabels
		/////////////////////////////////////////// + coreLabels
		
		coreLabel = new JLabel("кор");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		coreLabel.setBorder(BorderFactory.createBevelBorder(1));
		coreLabel.setBounds(new Rectangle(450, 200, 60, 30));
		window.add(coreLabel);
		
		suggestedCoreIcons = new JLabel[5];
		for(int i = 0; i < suggestedCoreIcons.length; i++) {
			suggestedCoreIcons[i] = new JLabel("" + i);
			suggestedCoreIcons[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedCoreIcons[i].setBounds(new Rectangle(430, 190 + (i+1)*55, 100, 50));
			window.add(suggestedCoreIcons[i]);
		}
		
		/////////////////////////////////////////// - coreLabels
		/////////////////////////////////////////// + exp
		
		controller.init();
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		input = "";
		updateCandidateName();
		updatePick();
		
		/////////////////////////////////////////// - exp
		
//		window.setUndecorated(true);
		window.setVisible(true);
	}
	
	private void run() {
		
		//Model.createTablesForAll();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Model model = new Model();
		MainWindow app = new MainWindow();
		Controller ctrl = new Controller();
		
		app.setController(ctrl);
		app.setModel(model);
		
		app.controller.setApp(app);
		app.controller.setModel(model);
		
		app.model.setApp(app);
		
//		app.init();
//		app.run();
		
//		SQLUtility.dictionaryFilling();
		SQLUtility.initAdvantageTables();
//		Model.setFriendAndFoes("abaddon");
	}
}