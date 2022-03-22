import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Model {
	
	private int currentIndex;
	private int[] pickOrder;
	
	private Hero[] wholePick;
	
	private Hero[] suggestedSupports;
	private Hero[] suggestedCores;
	
	private HashMap<String, Hero> allSupports;
	private HashMap<String, Hero> allCores;
	
	private MainWindow app;
	
	//private String candidateName = "";
	private Hero candidate;
	
	public Hero[] getPick() {
		return wholePick;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public Hero getCandidate() {
		return candidate;
	}
	
	public String getCandidateName() {
		return candidate.getName();
	}
	
	public void setCandidate(Hero hero){
		candidate = hero;
		app.updateCandidateName();
	}
		
	public void setApp(MainWindow a) {
		app = a;
	}
	
	public void guessCandidate(String name){
		candidate = Hero.guessFromInput(name);
		app.updateCandidateName();
	}
	
	public void initHeroBase() {
		candidate = Hero.createUnknown();
		
		suggestedSupports = new Hero[5];
		suggestedCores = new Hero[5];
		
		allCores = getCores();
		allSupports = getSupports();
	}
	
	public void initTeams() {
		wholePick = new Hero[10];          				//  from 0 to 4 are radiant Heroes and from 5 to 9 are Dire ones
		for(int i = 0; i < 10; i++) {
			wholePick[i] = Hero.createUnknown();
		}
	}
	
	public void updateSuggestions() {
		
		
		
	}
	
	private HashMap<String, Hero> getSupports() {			// actually returns not-cores
		HashMap<String, Hero> heroesMap = new HashMap<>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroesList = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroesList.next()) {
                	if(!heroesList.getString("role").equals("core"))
                		heroesMap.put(heroesList.getString("truename"), new Hero(heroesList.getString("truename"), heroesList.getString("role")));
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesMap;
	}
	
	private HashMap<String, Hero> getCores() {				// actually returns not-supports
		HashMap<String, Hero> heroesMap = new HashMap<>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroesList = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroesList.next()) {
                	if(!heroesList.getString("role").equals("supp"))
                		heroesMap.put(heroesList.getString("truename"), new Hero(heroesList.getString("truename"), heroesList.getString("role")));
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesMap;
	}
	
	private HashMap<String, Hero> getAllHeroes() {			// isn't used anywhere, but not having it is very strange considering getCores() and getSupport() exist
		HashMap<String, Hero> heroesMap = new HashMap<>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroesList = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroesList.next()) {
                	heroesMap.put(heroesList.getString("truename"), new Hero(heroesList.getString("truename"), heroesList.getString("role")));
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesMap;
	}
	
	public void addCandidate() throws IOException {						// sets a hero in the next in line pick slot 
		allCores.remove(candidate.getName());
		allSupports.remove(candidate.getName());
		if(currentIndex < 10 ) {
			for(int i = 0; i < currentIndex; i++) {    					// checking if pick already has this hero
				if(wholePick[pickOrder[i]].getName().equals(candidate.getName())) {     // to understand strange index operation see initPickOrder method below
					System.out.println("дубль геро€");
					return;
				}
			}
			if(getCandidateName() != "unknown") {
				System.out.println("добавили " + candidate.getName());
				wholePick[pickOrder[currentIndex]] = new Hero();						// to understand strange index operation see initPickOrder method below
				wholePick[pickOrder[currentIndex]].setName(candidate.getName());
//				System.out.println(candidate.getIcon());
				wholePick[pickOrder[currentIndex]].setIcon(candidate.getIcon());
				app.updatePick();
				currentIndex++;
				if (currentIndex < 10)
					app.updateNextSlotLabel(pickOrder[currentIndex]);
			} else {
				System.out.println("ѕредложение пустое");
			}
		}
	}
	
	public void initPickOrder() {
		currentIndex = 0;
		pickOrder = new int[] {0,1,5,6,2,3,7,8,4,9};   	// these are indexes for wholePick massive to add into, scheme is 2r-2d-2r-2d-1r-1d (Dota2 pub 2022y)
	}
	
}