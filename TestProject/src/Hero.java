import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Hero {
	private String name;
	private ImageIcon icon;
	private String role;
	
	public String getRole() {
		return this.role;
	}
	
	public ImageIcon getIcon() {
		return this.icon;
	}
	
	public void setIcon(ImageIcon ic) {
		icon = ic;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static boolean compare(String given, String fetched) {			//  given is a name user typed on the candidate label,
		char[] givenChars = given.toCharArray();							//  fetched is current jargon-column value from DB
		char[] fetchedChars = fetched.toCharArray();
		for(int i = 0; i < Math.min(fetchedChars.length, givenChars.length); i++) {
			if(fetchedChars[i] != givenChars[i])
				return false;
		}
		return true;
	}
	
	public static Hero createUnknown() {
		Hero res = new Hero();
		res.name = "unknown";
		res.setIcon(new ImageIcon("resources/icons/unknown.png"));
		return res;
	}
	
	public static Hero guessFromInput(String part) {
		Hero res = Hero.createUnknown();
		try {
            String url = SQLUtility.baseURL;
            String login = SQLUtility.login;
            String password = SQLUtility.password;
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM TRUENAMES");
                while (rs.next()) {
                	if(compare(part, rs.getString("jargon"))) {
                		res = new Hero();
                		res.name = rs.getString("truename");
                		ImageIcon i = new ImageIcon(ImageIO.read(new File("resources/icons/" + res.name + ".png")).getScaledInstance(128, 72, Image.SCALE_SMOOTH));
                		res.icon = i;
                	}
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
//		System.out.println("вернули нуль");
		return res;
	}
}