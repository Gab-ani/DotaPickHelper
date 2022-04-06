package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Controller {

	@Autowired
	DotaPickHelperApplication app;
	@Autowired
	Model model;
	@Autowired
	DraftWindow draftWindow;
	
	public void init() {
		initInputListener();
		initExitListener();
		initInfoListener();
		initResetListener();
		initSettingsListener();
	}
	
	public void initInputListener() {
		draftWindow.getCandidateLabel().addKeyListener(new KeyListener() {
			@Override
            public void keyReleased(KeyEvent event) {
            }

			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isLetter(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_SPACE) {
					draftWindow.setInput( draftWindow.getInput() + e.getKeyChar() );
					try {
						model.guessCandidate(draftWindow.getInput());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_BACK_SPACE)) {
					if(draftWindow.getInput().length() > 0) {
						draftWindow.setInput( draftWindow.getInput().substring( 0, draftWindow.getInput().length()-1 ) );
						if(draftWindow.getInput().length() != 0)
							try {
								model.guessCandidate(draftWindow.getInput());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						return;
					}
				}
				if(e.getKeyChar() == (KeyEvent.VK_DELETE)) {
					draftWindow.setInput("");
					try {
						model.setCandidate(Hero.createUnknown());
						draftWindow.updateCandidateName();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_ENTER)) {
					try {
						model.addCandidate();
					} catch (IOException | CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					try {
						model.setCandidate(Hero.createUnknown());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					draftWindow.setInput("");
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	public void initExitListener() {
		draftWindow.getExitButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				draftWindow.exit();
			}
			
		});
	}
	
	public void initSettingsListener() {
		draftWindow.getSettingsButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				draftWindow.showSettingsWindow();
			}
			
		});
	}
	
	public void initInfoListener() {
		draftWindow.getInfoButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				draftWindow.showInfo();
			}
			
		});
	}
	
	public void initResetListener() {
		draftWindow.getResetButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				app.reset();
			}
			
		});
	}
}
