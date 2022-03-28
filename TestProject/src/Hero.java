import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Hero implements Comparable<Hero>, Cloneable {
	
	private String name;
	private ImageIcon icon;
	private String role;
	private double advantage;
	public HashMap<String, Double> advantageTable;
	
	public Hero() {
		
	}
	
	public Hero(String name) {
		this.name = name;
//		this.icon = new ImageIcon(ImageIO.read(new File("resources/icons/" + name + ".png")).getScaledInstance(100, 72, Image.SCALE_SMOOTH))
		this.fetchAdvantageTable();
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
	
	public static Hero createUnknown() {											// creates a "blank" Hero object, which exists and could be used in methods,
		Hero res = new Hero();														// but doesn't represent any particular hero
		res.name = "unknown";
		res.setIcon(new ImageIcon("resources/icons/unknown.png"));
		res.advantageTable = new HashMap<String, Double>();
		return res;
	}
	
	public void calculateAdvantage(Hero[] pick) {									// calculates one hero advantage over pick
		advantage = 0;
		for(Hero h : pick) {
//			System.out.println(name + " имеет преимущество над " + h.name + " в " + advantageTable.getOrDefault(h.name, 0.0) + " очков");
			// TODO maybe other way around
			advantage += advantageTable.getOrDefault(h.name, 0.0);				
		}
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
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
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
                		res.fetchAdvantageTable();
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
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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