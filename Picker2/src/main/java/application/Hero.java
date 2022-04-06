package application;

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
	private String iconName;
	private String role;
	private double advantage;
	public HashMap<String, Double> advantageTable;
	
	public Hero() {
		
	}
	
	public Hero(String name) {
		this.name = name;
		this.fetchAdvantageTable();
		this.fetchIcon();
	}
	
	public Hero(String name, String role) {
		this.name = name;
		this.role = role;
		
		this.fetchAdvantageTable();
		this.fetchIcon();
	}
	
	public String getRole() {
		return this.role;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getIconName() {
		return this.iconName;
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
		res.fetchIcon();
		res.advantageTable = new HashMap<String, Double>();
		return res;
	}
	
	public void calculateAdvantage(Hero[] pick) {									// calculates one hero advantage over pick
		advantage = 0;
		for(Hero h : pick) {
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
	
	private void fetchIcon() {
		this.iconName = "src/main/resources/heroIcons/" + this.name + ".png";
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
						res = new Hero(rs.getString("truename"));
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
