import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Model {
	
	private Hero[] wholePick;
	private int currentIndex;
	private int[] pickOrder;
	
	private Hero[] suggestedSupportsRadiant;						
	private Hero[] suggestedCoresRadiant;
	private Hero[] suggestedSupportsDire;
	private Hero[] suggestedCoresDire;
	
	private ArrayList<Hero> allSupports;							// actually all non-cores
	private ArrayList<Hero> allCores;								// actually all non-supports, some heroes have third role "both" and stored in both maps
	
	private MainWindow app;
	
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
		
		suggestedSupportsRadiant = new Hero[5];
		suggestedCoresRadiant = new Hero[5];
		suggestedSupportsDire = new Hero[5];
		suggestedCoresDire = new Hero[5];
		
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
	
//	private void calculateAdvantage(Hero hero) {									// calculates one hero advantage over pick
//		HashMap<Hero, Double> advantages = SQLUtility.getAdvantageTable(hero);
//		for(int i = 0; i < wholePick.length; i++) {
//			hero.setAdvantage(hero.getAdvantage() + );
//		}
//	}
	
	private ArrayList<Hero> getSupports() {			// returns list of all not-cores from DB
		ArrayList<Hero> heroesList = new ArrayList<Hero>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroes = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroes.next()) {
                	if(!heroes.getString("role").equals("core")) {
                		heroesList.add(new Hero(heroes.getString("truename"), heroes.getString("role")));
                	}
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesList;
	}
	
	private ArrayList<Hero> getCores() {				// returns list of all not-supports from DB
		ArrayList<Hero> heroesList = new ArrayList<>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroes = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroes.next()) {
                	if(!heroes.getString("role").equals("supp"))
                		heroesList.add(new Hero(heroes.getString("truename"), heroes.getString("role")));
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesList;
	}
	
	private ArrayList<Hero> getAllHeroes() {			// isn't used anywhere, but not having it is very strange considering getCores() and getSupport() exist
		ArrayList<Hero> heroesList = new ArrayList<>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroes = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroes.next()) {
                	heroesList.add(new Hero(heroes.getString("truename"), heroes.getString("role")));
                }
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesList;
	}
	
	public void addCandidate() throws IOException {						// sets a hero in the next in line pick slot 
		allCores.remove(candidate);
		allSupports.remove(candidate);
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
				if (currentIndex < 10) {
					app.updateNextSlotLabel(pickOrder[currentIndex]);
				}
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