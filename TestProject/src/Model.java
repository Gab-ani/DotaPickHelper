import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Model {
	
	private int currentIndex;
	private int[] pickOrder;
	
	private Hero[] wholePick;
	
	private Hero[] supportRow;
	private Hero[] coreRow;
	
	private MainWindow app;
	
	//private String candidateName = "";
	private Hero candidate;
	
	//////////////////////////////////////////////////////// + GETTERS
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
	//////////////////////////////////////////////////////// - GETTERS
	//////////////////////////////////////////////////////// + SETTERS
	
	public void setCandidate(Hero hero){
		candidate = hero;
		app.updateCandidateName();
	}
		
	public void setApp(MainWindow a) {
		app = a;
	}
	//////////////////////////////////////////////////////// - SETTERS
	
	public void guessCandidate(String name){
		candidate = Hero.guessFromInput(name);
		app.updateCandidateName();
	}
	
	public void initHeroBase() {
		candidate = Hero.createUnknown();
	}
	
	public void initTeams() {
		wholePick = new Hero[10];          				//  from 0 to 4 are radiant Heroes and from 5 to 9 are Dire ones
		for(int i = 0; i < 10; i++) {
			wholePick[i] = Hero.createUnknown();
		}
	}
	
//	public void updateCandidate() {
//		
//	}
	
	public void addCandidate() throws IOException {						// sets a hero in the next in line pick slot 
		if(currentIndex < 10 ) {
			for(int i = 0; i < currentIndex; i++) {     // checking if pick already has this hero
				if(wholePick[pickOrder[i]].getName().equals(candidate.getName())) {     // to understand strange index operation see initPickOrder method below
					System.out.println("����� �����");
					return;
				}
			}
			if(getCandidateName() != "unknown") {
				System.out.println("�������� " + candidate.getName());
				wholePick[pickOrder[currentIndex]] = new Hero();						// to understand strange index operation see initPickOrder method below
				wholePick[pickOrder[currentIndex]].setName(candidate.getName());
//				System.out.println(candidate.getIcon());
				wholePick[pickOrder[currentIndex]].setIcon(candidate.getIcon());
				app.updatePick();
				currentIndex++;
				if (currentIndex < 10)
					app.updateNextSlotLabel(pickOrder[currentIndex]);
			} else {
				System.out.println("����������� ������");
			}
		}
	}
	
	public void initPickOrder() {
		currentIndex = 0;
		pickOrder = new int[] {0,1,5,6,2,3,7,8,4,9};   	// these are indexes for wholePick massive to add into, scheme is 2r-2d-2r-2d-1r-1d (Dota2 pub 2022y)
	}
	
	public static void setFriendAndFoes(String truename, boolean exists) {
		try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Dota2Picker";
            String login = "postgres";
            String password = "pstgrs2022gfhjkm";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
            	
                Scanner scan = new Scanner(System.in);

                
//                for(int i = 5; i > 0; i--) {
//                	con.createStatement().executeUpdate("update " + truename + " set heroname = '" + scan.nextLine() + "' where advantage = " + i);
//                }
//                for(int i = -5; i < 0; i++) {
//                	con.createStatement().executeUpdate("update " + truename + " set heroname = '" + scan.nextLine() + "' where advantage = " + i);
//                }
                
                
                scan.close();
                
            } finally {
                con.close();
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}