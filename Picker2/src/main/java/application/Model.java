package application;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Model {
	
	@Autowired
	private DraftWindow draftWindow;
	@Autowired
	private PickerDAO pickerDAO;
	
	private Hero[] wholePick;
	private int currentIndex;
	private int[] pickOrder;
	
	private ArrayList<ArrayList<Hero>> radiantSuggestionPool;
	private ArrayList<ArrayList<Hero>> direSuggestionPool;

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

	public ArrayList<ArrayList<Hero>> getRadiantSuggestionPool() {
		return radiantSuggestionPool;
	}
	
	public ArrayList<ArrayList<Hero>> getDireSuggestionPool() {
		return direSuggestionPool;
	}
	
	public void setCandidate(Hero hero) throws IOException{
		candidate = hero;
		draftWindow.updateCandidateName();
	}
	
	public void guessCandidate(String name) throws IOException{
		candidate = guessFromInput(name);
		draftWindow.updateCandidateName();
	}
	
	public void initHeroBase() {
		candidate = Hero.createUnknown();
		radiantSuggestionPool = new ArrayList<ArrayList<Hero>>();
		direSuggestionPool = new ArrayList<ArrayList<Hero>>();
		for(int i = 1; i < 6; i++) {
			radiantSuggestionPool.add(selectByRole(i));
			direSuggestionPool.add(selectByRole(i));
		}
		
	}
	
	public void initTeams() {
		wholePick = new Hero[10];          				//  from 0 to 4 are radiant Heroes and from 5 to 9 are Dire ones
		for(int i = 0; i < 10; i++) {
			wholePick[i] = Hero.createUnknown();
		}
	}
	
	public void updateSuggestions() throws IOException {
		for(int i = 0; i < 5; i++) {
			radiantSuggestionPool.get(i).forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 5, 10)));
			radiantSuggestionPool.get(i).sort(null);
			Collections.reverse(radiantSuggestionPool.get(i));
			direSuggestionPool.get(i).forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 0, 5)));
			direSuggestionPool.get(i).sort(null);
			Collections.reverse(direSuggestionPool.get(i));
			
		}
		draftWindow.updateSuggestions();
	}
	
	private ArrayList<Hero> selectByRole(int i) {					// i can only be from 1 to 5 and presents role of a hero
		ArrayList<Hero> heroesList = new ArrayList<Hero>();
		try {
			Connection con = pickerDAO.getConnection();
			Statement getHeroes = con.createStatement();
            ResultSet heroes = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
            while (heroes.next()) {
            	String role = i + "";
            	if(heroes.getString("role").contains(role)) {
            		Hero hero = new Hero(heroes.getString("truename"));
            		setAdvantageTable(hero);
            		heroesList.add(hero);
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroesList;
	}
	
	public Hero guessFromInput(String part) {
		Hero res = Hero.createUnknown();
		
		try {
			Connection con = pickerDAO.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM TRUENAMES");
			while (rs.next()) {
				if(compareWithInput(part, rs.getString("jargon"))) {
					res = new Hero(rs.getString("truename"));
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
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
	
	public void addCandidate() throws IOException, CloneNotSupportedException {						// sets a hero in the next-in-line pick slot 
		for(int i = 0; i < 5; i++) {
			removeByName(radiantSuggestionPool.get(i), candidate.getName());
			removeByName(direSuggestionPool.get(i), candidate.getName());
		}
		
		if(currentIndex < 10 ) {
			for(int i = 0; i < currentIndex; i++) {    									// checking if pick already has this hero and halting process if positive
				if(wholePick[pickOrder[i]].getName().equals(candidate.getName())) {     // to understand strange index operation see initPickOrder method below
//					System.out.println("дубль героя");									// TODO logger?
					return;
				}
			}
			if(getCandidateName() != "unknown") {
				System.out.println("добавили " + candidate.getName());
				wholePick[pickOrder[currentIndex]] = new Hero(candidate.getName());		// to understand strange index operation see initPickOrder method below
				setAdvantageTable(wholePick[pickOrder[currentIndex]]);
				draftWindow.updatePick();
				updateSuggestions();
				currentIndex++;
				if (currentIndex < 10) {
					draftWindow.updateNextSlotLabel(pickOrder[currentIndex]);
				}
			} else {
//				System.out.println("Предложение пустое");								// TODO logger?
			}
		}
		
		if(currentIndex == 10)
			draftWindow.showFinalAdvantages();
	}
	
	private void setAdvantageTable(Hero hero) {								// Initializes advantageTable field
		HashMap<String, Double> table = new HashMap<>();
		try {
            Connection con = pickerDAO.getConnection();
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM " + hero.getName());
            while (res.next()) {
            	table.put(res.getString("hero"), res.getDouble("advantage"));
            }
            res.close();
            stmt.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
		hero.setAdvantageTable(table);
	}
	
	public static String toDotabuffNamingRules(String name) {	// util method, mades a string to to match web address on dotabuff
		name = name.replaceAll("[_]", "-");
		return name;
	}
	
	public static String toDBNamingRules(String name) {	// util method, mades a string to to match naming rules in the DB
		name = name.replaceAll("[-]", "");
		name = name.replaceAll("[']", "");
		name = name.replaceAll("[ ]", "_");
		name = name.toLowerCase();
		return name;
	}
	
	private void removeByName(ArrayList<Hero> col, String name) {
		for(int i = 0; i < col.size(); i++) {
			if (col.get(i).getName().equals(name)) {
				col.remove(i);
			}
		}
	}
	
	public void initPickOrder() {
		currentIndex = 0;
		pickOrder = new int[] {0,1,5,6,2,3,7,8,4,9};   	// these are indexes for wholePick massive to add into, scheme is 2r-2d-2r-2d-1r-1d (Dota2 pub 2022y)
	}
}
