package application;

import java.util.HashMap;

public class Hero implements Comparable<Hero>, Cloneable {

	private String name;
	private String iconName;
	private String role;
	private double advantage;
	private HashMap<String, Double> advantageTable;
	
	public Hero() {
		
	}
	
	public Hero(String name) {
		this.name = name;
		this.fetchIcon();
	}
	
	public Hero(String name, String role) {
		this.name = name;
		this.role = role;

		this.fetchIcon();
	}
	
	public void setAdvantageTable(HashMap<String, Double> map) {
		advantageTable = map;
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
	
	private void fetchIcon() {
		this.iconName = "src/main/resources/heroIcons/" + this.name + ".png";
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
