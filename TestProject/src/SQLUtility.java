import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class SQLUtility {
	
	public static final String driver = "org.postgresql.Driver";
	public static final String baseURL = "jdbc:postgresql://localhost:5432/Dota2Picker";
	public static final String login = "postgres";
	public static final String password = "pstgrs2022gfhjkm";
	
	public static void dictionaryFilling() {					// adds new jargon-truename pairs in a cycle
		System.out.println("type 'end' to exit");
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()) {
			String jargon = scan.nextLine();
			if(jargon.equals("end")) {
				break;
			}
			String truename = scan.nextLine();
			if(truename.equals("end")) {
				break;
			}
			insertTruenamePair(jargon, truename);
		}
		scan.close();
		return;
	}
	
	public static void createRoleColumn() {
		
	}
	
	public static void initAdvantageTables() throws ClassNotFoundException {				// this intended to be used once in a lifetime to create a batch of empty tables,
		Class.forName(SQLUtility.driver);													// a table for every unique name in truenames table (read for every hero in dota2),
		try {																				// those tables will contain data about heroes counterpicks
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
                Statement getHeroes = con.createStatement();
                ResultSet heroesList = getHeroes.executeQuery("select distinct truename from truenames order by truename asc");
                while (heroesList.next()) {
                	System.out.println(heroesList.getString("truename"));
                	Statement createTable = con.createStatement();
                	try {                
                		System.out.println("create table " + heroesList.getString("truename") + " ( hero text, advantage real )");
                    	createTable.executeQuery("create table " + heroesList.getString("truename") + " ( hero text, advantage real )");
                	} catch (Exception e) {
                		//e.printStackTrace();
                		System.out.println("no return information");                        // always procs exception because query didn't return anything.
                		//createTable.close();												// I don't know how to escape this, so just ignoring it - tables are created anyway
                    }
                	createTable.close();
                }
                heroesList.close();
                getHeroes.close();
            } finally {
                con.close();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void advantageSetProcess() {          // TODO spirit breaker
		System.out.println("введите truename для героя");
		Scanner scan = new Scanner(System.in);
		String inputName = scan.nextLine();
		if(!tableExists(inputName)) {
			System.out.println("герой не найден");
			scan.close();
			return;
		}
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				for(int i = 0; i < 10; i++) {
					String name = scan.next();
					double advantage = scan.nextDouble();
					Statement update = con.createStatement();
					update.executeUpdate("INSERT INTO " + inputName + " VALUES ('" + name + "', " + advantage + ")");
				}
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		scan.close();
	}
	
	private static boolean tableExists(String name) {
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				Statement checkTable = con.createStatement();
				ResultSet checkResults = checkTable.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='" + name + "'");
				if(checkResults.next()) {
					return true;
				}
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void insertTruenamePair(String jargon, String truename) {					// adds a new jargon-truename relation in the DB
		try {
			Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
			try {
				String sql = "INSERT INTO TRUENAMES (JARGON, TRUENAME) VALUES (?, ?)";
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, jargon);
				stmt.setString(2, truename.replace(' ', '_'));
				stmt.executeUpdate();
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
