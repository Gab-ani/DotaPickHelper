import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Model {
	
	private Hero[] wholePick;
	private int currentIndex;
	private int[] pickOrder;
	
	public ArrayList<Hero> radiantSupports;
	public ArrayList<Hero> radiantCores;
	public ArrayList<Hero> direSupports;
	public ArrayList<Hero> direCores;
	
	private View app;
	
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
		app.mainWindow.updateCandidateName();
	}
		
	public void setApp(View a) {
		app = a;
	}
	
	public void guessCandidate(String name){
		candidate = Hero.guessFromInput(name);
		app.mainWindow.updateCandidateName();
	}
	
	public void initHeroBase() {
		candidate = Hero.createUnknown();
		
		radiantSupports = getSupports();
		radiantCores = getCores();
		direSupports = getSupports();
		direCores = getCores();
	}
	
	public void initTeams() {
		wholePick = new Hero[10];          				//  from 0 to 4 are radiant Heroes and from 5 to 9 are Dire ones
		for(int i = 0; i < 10; i++) {
			wholePick[i] = Hero.createUnknown();
		}
	}
	
	public void updateSuggestions() throws IOException {
	
	direCores.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 0, 5)));
	direSupports.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 0, 5)));
	radiantCores.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 5, 10)));
	radiantSupports.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 5, 10)));

	direCores.sort(null);
	Collections.reverse(direCores);
	direSupports.sort(null);
	Collections.reverse(direSupports);
	radiantCores.sort(null);
	Collections.reverse(radiantCores);
	radiantSupports.sort(null);
	Collections.reverse(radiantSupports);
	app.mainWindow.updateSuggestions();
	}
	
	private ArrayList<Hero> getSupports() {			// returns list of all not-cores from DB
		ArrayList<Hero> heroesList = new ArrayList<Hero>();
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement getHeroes = con.createStatement();
                ResultSet heroes = getHeroes.executeQuery("select distinct truename, role from truenames order by truename asc");
                while (heroes.next()) {
                	if(!heroes.getString("role").equals("core"))
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
	
	public void addCandidate() throws IOException, CloneNotSupportedException {						// sets a hero in the next-in-line pick slot 

		removeByName(radiantCores, candidate.getName());
		removeByName(radiantSupports, candidate.getName());
		removeByName(direCores, candidate.getName());
		removeByName(direSupports, candidate.getName());
		if(currentIndex < 10 ) {
			for(int i = 0; i < currentIndex; i++) {    									// checking if pick already has this hero and halting process if positive
				if(wholePick[pickOrder[i]].getName().equals(candidate.getName())) {     // to understand strange index operation see initPickOrder method below
					System.out.println("дубль геро€");
					return;
				}
			}
			if(getCandidateName() != "unknown") {
				System.out.println("добавили " + candidate.getName());
				wholePick[pickOrder[currentIndex]] = new Hero(candidate.getName());		// to understand strange index operation see initPickOrder method below
				wholePick[pickOrder[currentIndex]].setIcon(candidate.getIcon());
				app.mainWindow.updatePick();
				updateSuggestions();
				currentIndex++;
				if (currentIndex < 10) {
					app.mainWindow.updateNextSlotLabel(pickOrder[currentIndex]);
				}
			} else {
				System.out.println("ѕредложение пустое");
			}
		}
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