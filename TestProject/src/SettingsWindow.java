import javax.swing.JFrame;

public class SettingsWindow {

	static final int width = 400;
	static final int height = 300;
	
	public void setupWindow() {
		JFrame settings = new JFrame();
		settings.setSize(width, height);
		settings.setVisible(true);
	}
	
}
