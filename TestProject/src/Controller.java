import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Controller {
	
	private Model logic;
	private MainWindow app;
	
	public void setApp(MainWindow w) {
		app = w;
	}
	
	public void setModel(Model m) {
		logic = m;
	}
	
	public MainWindow getApp() {
		return app;
	}
	
	public Model getModel() {
		return logic;
	}
	
	public void init() {
		app.getCandidateLabel().addKeyListener(new KeyListener() {
			@Override
            public void keyReleased(KeyEvent event) {
            }

			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isLetter(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_SPACE) {
					app.setInput( app.getInput() + e.getKeyChar() );
					logic.guessCandidate(app.getInput());
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_BACK_SPACE)) {
					if(app.getInput().length() > 0) {
						app.setInput(app.getInput().substring(0, app.getInput().length() - 1));
						if(app.getInput().length() != 0)
							logic.guessCandidate(app.getInput());
						return;
					}
				}
				if(e.getKeyChar() == (KeyEvent.VK_DELETE)) {
					app.setInput("");
					logic.setCandidate(Hero.createUnknown());
					app.updateCandidateName();
					return;
				}
				if(e.getKeyChar() == (KeyEvent.VK_ENTER)) {
					try {
						logic.addCandidate();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					logic.setCandidate(Hero.createUnknown());
					app.setInput("");
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
}