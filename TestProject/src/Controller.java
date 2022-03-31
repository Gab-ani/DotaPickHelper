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
		app.mainWindow.getCandidateLabel().addKeyListener(new KeyListener() {
			@Override
            public void keyReleased(KeyEvent event) {
            }

			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isLetter(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_SPACE) {
					app.mainWindow.setInput( app.mainWindow.getInput() + e.getKeyChar() );
					logic.guessCandidate(app.mainWindow.getInput());
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_BACK_SPACE)) {
					if(app.mainWindow.getInput().length() > 0) {
						app.mainWindow.setInput( app.mainWindow.getInput().substring( 0, app.mainWindow.getInput().length()-1 ) );
						if(app.mainWindow.getInput().length() != 0)
							logic.guessCandidate(app.mainWindow.getInput());
						return;
					}
				}
				if(e.getKeyChar() == (KeyEvent.VK_DELETE)) {
					app.mainWindow.setInput("");
					logic.setCandidate(Hero.createUnknown());
					app.mainWindow.updateCandidateName();
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_ENTER)) {
					try {
						logic.addCandidate();
					} catch (IOException | CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					logic.setCandidate(Hero.createUnknown());
					app.mainWindow.setInput("");
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
}