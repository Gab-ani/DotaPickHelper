import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class Model {
	
	private Hero[] wholePick;
	private int currentIndex;
	private int[] pickOrder;
	
	public Hero[] suggestedSupportsRadiant;						// these are model-part of 20 suggested labels in MainWindow, 
	public Hero[] suggestedCoresRadiant;							// they will store top 5 of sorted out heroes from allSupports and allCores
	public Hero[] suggestedSupportsDire;
	public Hero[] suggestedCoresDire;
	
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
	
	public void updateSuggestions() throws IOException {
		
		allSupports.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 0, 5)));
		allCores.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 0, 5)));
		
		allSupports.sort(null);
		allCores.sort(null);
		
		for(int i = 0; i < 5; i++) {
			suggestedSupportsDire[i] = allSupports.get(allSupports.size() - i - 1);
			suggestedCoresDire[i] = allCores.get(allCores.size() - i - 1);
		}
		
		allSupports.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 5, 10)));
		allCores.forEach(hero -> hero.calculateAdvantage(Arrays.copyOfRange(wholePick, 5, 10)));
		
		allSupports.sort(null);
		allCores.sort(null);
		
		for(int i = 0; i < 5; i++) {
			suggestedSupportsRadiant[i] = allSupports.get(allSupports.size() - i - 1);
			suggestedCoresRadiant[i] = allCores.get(allCores.size() - i - 1);
		}
	
		app.updateSuggestions();
		
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
		allCores.remove(candidate);
		allSupports.remove(candidate);
		if(currentIndex < 10 ) {
			for(int i = 0; i < currentIndex; i++) {    								// checking if pick already has this hero
				if(wholePick[pickOrder[i]].getName().equals(candidate.getName())) {     // to understand strange index operation see initPickOrder method below
					System.out.println("дубль геро€");
					return;
				}
			}
			if(getCandidateName() != "unknown") {
				System.out.println("добавили " + candidate.getName());
				wholePick[pickOrder[currentIndex]] = new Hero(candidate.getName());		// to understand strange index operation see initPickOrder method below
				wholePick[pickOrder[currentIndex]].setIcon(candidate.getIcon());
				
//				for(int i = 0; i < 10; i++) {
//					System.out.println(wholePick[i].advantageTable.toString());
//				}
				
				app.updatePick();
				currentIndex++;
				if (currentIndex < 10) {
					app.updateNextSlotLabel(pickOrder[currentIndex]);
				}
				updateSuggestions();
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