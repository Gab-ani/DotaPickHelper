import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Controller {
	
	private Model logic;
	private View app;
	
	public void setApp(View w) {
		app = w;
	}
	
	public void setModel(Model m) {
		logic = m;
	}
	
	public View getApp() {
		return app;
	}
	
	public Model getModel() {
		return logic;
	}
	
	public void init() {
		
		initInputListener();
		initExitListener();
		initInfoListener();
		initResetListener();
		initSettingsListener();
		
	}
	
	public void initInputListener() {
		app.mainWindow.getCandidateLabel().addKeyListener(new KeyListener() {
			@Override
            public void keyReleased(KeyEvent event) {
            }

			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isLetter(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_SPACE) {
					app.mainWindow.setInput( app.mainWindow.getInput() + e.getKeyChar() );
					try {
						logic.guessCandidate(app.mainWindow.getInput());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_BACK_SPACE)) {
					if(app.mainWindow.getInput().length() > 0) {
						app.mainWindow.setInput( app.mainWindow.getInput().substring( 0, app.mainWindow.getInput().length()-1 ) );
						if(app.mainWindow.getInput().length() != 0)
							try {
								logic.guessCandidate(app.mainWindow.getInput());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						return;
					}
				}
				if(e.getKeyChar() == (KeyEvent.VK_DELETE)) {
					app.mainWindow.setInput("");
					try {
						logic.setCandidate(Hero.createUnknown());
						app.mainWindow.updateCandidateName();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_ENTER)) {
					try {
						logic.addCandidate();
					} catch (IOException | CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					try {
						logic.setCandidate(Hero.createUnknown());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					app.mainWindow.setInput("");
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	public void initExitListener() {
		app.mainWindow.getExitButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				app.mainWindow.exit();
			}
			
		});
	}
	
	public void initSettingsListener() {
		app.mainWindow.getSettingsButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				app.mainWindow.showSettingsWindow();
			}
			
		});
	}
	
	public void initInfoListener() {
		app.mainWindow.getInfoButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				app.mainWindow.showInfo();
			}
			
		});
	}
	
	public void initResetListener() {
		app.mainWindow.getResetButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					app.reset();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
	}
	
}