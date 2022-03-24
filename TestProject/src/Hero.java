import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Hero implements Comparable<Hero> {
	
	private String name;
	private ImageIcon icon;
	private String role;
	private double advantage;
	private HashMap<String, Double> advantageTable;
	
	public Hero() {
		
	}
	
	public Hero(String name, String role) {
		this.name = name;
		this.role = role;
		
		this.fetchAdvantageTable();
	}
	
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
	
	public void setAdvantage(double adv) {
		advantage = adv;
	
	}
	
	public double getAdvantage() {
		return this.advantage;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static boolean compareWithInput(String given, String fetched) {			//  given is a name user typed on the candidate label,
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
	
	private void fetchAdvantageTable() {								// Initializes advantageTable field
		this.advantageTable = new HashMap<>();
		try {
            Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
            try {
                Statement stmt = con.createStatement();
                ResultSet res = stmt.executeQuery("SELECT * FROM " + this.name);
                while (res.next()) {
                	advantageTable.put(res.getString("hero"), res.getDouble("advantage"));
                }
                res.close();
                stmt.close();
            } finally {
                con.close();
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
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
                	if(compareWithInput(part, rs.getString("jargon"))) {
                		res = new Hero();
                		res.name = rs.getString("truename");
//                		System.out.println(rs.getString("truename"));
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

	@Override
	public int compareTo(Hero h) {
		if(advantage < h.advantage)
			return -1;
		if(advantage > h.advantage)
			return 1;
		return 0;
	}
}