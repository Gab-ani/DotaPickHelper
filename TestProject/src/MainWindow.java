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
	final static Rectangle exitRectangle = new Rectangle(windowWidth / 2 - 170, windowHeight / 2 - 30, 50, 30);
	
	public View fatherView;
	
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
	
	private JButton resetButton;
	private JButton settingsButton;
	private JButton exitButton;
	private JButton infoButton;
	
	public void updateNextSlotLabel(int index) {
		nextHeroSlot.setLocation(pickIcons[index].getLocation().x + heroIconWidth / 2 - nextHeroSlot.getWidth() / 2, pickIcons[index].getLocation().y + heroIconHeight + 10);
	}
	
	public void updatePick() throws IOException {
		for(int i = 0; i < 10; i++) {
			pickIcons[i].setIcon(fatherView.model.getPick()[i].getIcon());
		}
	}
	
	public void updateSuggestions() throws IOException {
		for(int i = 0; i < 5; i++) {
			if(!fatherView.model.getPick()[5].getName().equals("unknown")) {	// checks if dire pick is even initialized right now and blocks radiant recommendations from appear
				suggestedSupportRadiant[i].setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + fatherView.model.radiantSupports.get(i).getName() + ".png")).getScaledInstance(128, 72, Image.SCALE_SMOOTH)));
				suggestedCoreRadiant[i].setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + fatherView.model.radiantCores.get(i).getName() + ".png")).getScaledInstance(128, 72, Image.SCALE_SMOOTH)));
			}
			suggestedSupportDire[i].setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + fatherView.model.direSupports.get(i).getName() + ".png")).getScaledInstance(128, 72, Image.SCALE_SMOOTH)));		
			suggestedCoreDire[i].setIcon(new ImageIcon(ImageIO.read(new File("resources/icons/" + fatherView.model.direCores.get(i).getName() + ".png")).getScaledInstance(128, 72, Image.SCALE_SMOOTH)));
		}
	}
	
	public void updateInputLabel() {
		inputLabel.setText(input);
	}
	
	public void updateCandidateName() {
		ImageIcon icon = new ImageIcon("resources/icons/" + fatherView.model.getCandidate().getName() + ".png");
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
	
	public void setView(View v) {
		fatherView = v;
	}
	
	public void setInput(String s) {
		input = s;
		updateInputLabel();
	}
	
	public void setupWindow() throws IOException {
		
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
				
		supportLabelRadiant = new JLabel("сап Radiant");
//		supportLabel.setIcon(new ImageIcon("icons/support.png"));
		supportLabelRadiant.setBorder(BorderFactory.createBevelBorder(1));
		supportLabelRadiant.setBounds(new Rectangle(200, 200, 60, 30));
		window.add(supportLabelRadiant);
		
		suggestedSupportRadiant = new JLabel[5];
		for(int i = 0; i < suggestedSupportRadiant.length; i++) {
			suggestedSupportRadiant[i] = new JLabel("" + i);
			suggestedSupportRadiant[i].setIcon(new ImageIcon("resources/icons/unknown.png"));
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
			suggestedCoreRadiant[i].setIcon(new ImageIcon("resources/icons/unknown.png"));
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
			suggestedSupportDire[i].setIcon(new ImageIcon("resources/icons/unknown.png"));
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
			suggestedCoreDire[i].setIcon(new ImageIcon("resources/icons/unknown.png"));
			suggestedCoreDire[i].setBounds(new Rectangle(1230, 190 + (i+1)*55, 100, 50));
			window.add(suggestedCoreDire[i]);
		}
		
		exitButton = new JButton();
		exitButton.setBounds(exitRectangle);
		window.add(exitButton);
		
		input = "";
		updateCandidateName();
		updatePick();
		
//		window.setUndecorated(true);
		window.setVisible(true);
	}
	
}
