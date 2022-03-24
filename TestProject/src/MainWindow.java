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
import java.util.Collection;

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
	
	final static int windowWidth = 1500;
	final static int windowHeight = 600;   			// global window
	
	final static int heroIconWidth = 128;
	final static int heroIconHeight = 72;			// pick icons
	final static int heroIconSpace = 12;		
	final static int heroIconStartSpaceX = 20;
	final static int heroIconStartSpaceY = 30;
	
	final static int candIconWidth = 256;			// central icon
	final static int candIconHeight = 144;
	
	final static Rectangle textRect = new Rectangle(windowWidth / 2 - 145, windowHeight / 2 + 75, 290, 150);
	
	Controller controller;
	Model model;
	
	private JLabel[] pickIcons;
	
	private JLabel[] suggestedSupportRadiant;
	private JLabel[] suggestedCoreRadiant;
	private JLabel[] suggestedSupportDire;
	private JLabel[] suggestedCoreDire;
	
	private JButton candidateLabel;
	
	private String input;
	private JLabel inputLabel;
	
	private JLabel nextHeroSlot;
	
	private JLabel supportLabelRadiant;
	private JLabel coreLabelRadiant;
	private JLabel supportLabelDire;
	private JLabel coreLabelDire;
	
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
		ImageIcon icon = new ImageIcon("resources/icons/" + model.getCandidate().getName() + ".png");
		Image imageToRescale = icon.getImage().getScaledInstance(256, 144, Image.SCALE_SMOOTH);
		icon = new ImageIcon(imageToRescale);
		//ImageIcon i = model.getCandidate().getIcon();
		getCandidateLabel().setIcon(icon);
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
	
	private void init() throws IOException {
		
		JFrame window = new JFrame("Dota2Picker");
		window.setBounds(new Rectangle(0, 0, windowWidth, windowHeight));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setResizable(false);
		
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
		candidateLabel.setBounds(new Rectangle(window.getWidth() / 2 - candIconWidth / 2, window.getHeight() / 2 - candIconHeight / 2 - 50, candIconWidth, candIconHeight));
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
				
		supportLabelRadiant = new JLabel("сап Radiant");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		supportLabelRadiant.setBorder(BorderFactory.createBevelBorder(1));
		supportLabelRadiant.setBounds(new Rectangle(200, 200, 60, 30));
		window.add(supportLabelRadiant);
		
		suggestedSupportRadiant = new JLabel[5];
		for(int i = 0; i < suggestedSupportRadiant.length; i++) {
			suggestedSupportRadiant[i] = new JLabel("" + i);
			suggestedSupportRadiant[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedSupportRadiant[i].setBounds(new Rectangle(180, 190 + (i+1)*55, 100, 50));
			window.add(suggestedSupportRadiant[i]);
		}
		
		coreLabelRadiant = new JLabel("кор Radiant");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		coreLabelRadiant.setBorder(BorderFactory.createBevelBorder(1));
		coreLabelRadiant.setBounds(new Rectangle(400, 200, 60, 30));
		window.add(coreLabelRadiant);
		
		suggestedCoreRadiant = new JLabel[5];
		for(int i = 0; i < suggestedCoreRadiant.length; i++) {
			suggestedCoreRadiant[i] = new JLabel("" + i);
			suggestedCoreRadiant[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedCoreRadiant[i].setBounds(new Rectangle(380, 190 + (i+1)*55, 100, 50));
			window.add(suggestedCoreRadiant[i]);
		}
		
		supportLabelDire = new JLabel("сап Dire");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		supportLabelDire.setBorder(BorderFactory.createBevelBorder(1));
		supportLabelDire.setBounds(new Rectangle(1050, 200, 60, 30));
		window.add(supportLabelDire);
		
		suggestedSupportDire = new JLabel[5];
		for(int i = 0; i < suggestedSupportDire.length; i++) {
			suggestedSupportDire[i] = new JLabel("" + i);
			suggestedSupportDire[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedSupportDire[i].setBounds(new Rectangle(1030, 190 + (i+1)*55, 100, 50));
			window.add(suggestedSupportDire[i]);
		}
		
		coreLabelDire = new JLabel("кор Dire");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		coreLabelDire.setBorder(BorderFactory.createBevelBorder(1));
		coreLabelDire.setBounds(new Rectangle(1250, 200, 60, 30));
		window.add(coreLabelDire);
		
		suggestedCoreDire = new JLabel[5];
		for(int i = 0; i < suggestedCoreDire.length; i++) {
			suggestedCoreDire[i] = new JLabel("" + i);
			suggestedCoreDire[i].setBorder(BorderFactory.createBevelBorder(1));
			suggestedCoreDire[i].setBounds(new Rectangle(1230, 190 + (i+1)*55, 100, 50));
			window.add(suggestedCoreDire[i]);
		}
		
		
		
		
		
		
		controller.init();
		model.initPickOrder();
		model.initTeams();
		model.initHeroBase();
		input = "";
		updateCandidateName();
		updatePick();
		
//		window.setUndecorated(true);
		window.setVisible(true);
	}
	
	private void run() {
		
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
		
		app.init();
		app.run();
		
	}
}