package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class PickerDAO {
	
	private static final String driver = "org.postgresql.Driver";
	private static final String baseURL = "jdbc:postgresql://localhost:5432/Dota2Picker";
	private static final String login = "postgres";
	private static final String password = "pstgrs2022gfhjkm";
	
	private Connection connection;
	
	public Connection getConnection() {
		return connection;
	}

	public PickerDAO() throws SQLException {
		this.connection = initConnection();
	}
	
	private Connection initConnection() throws SQLException {
		return DriverManager.getConnection(baseURL, login, password);
	}
	
	public void dictionaryFilling() {				// adds new jargon-truename pairs in a cycle
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
	
	public void setRolesColumn() {					// method creates values for column "role" in truenames table in cycle
		Scanner scan = new Scanner(System.in);				// order of work - read hero name then type a number [12345]
		try {
			Statement getHeroes = connection.createStatement();
            ResultSet heroesList = getHeroes.executeQuery("select distinct truename from truenames order by truename asc");
            while (heroesList.next()) {
            	System.out.println(heroesList.getString("truename") + "?");
            	Statement insertRole = connection.createStatement();
            	insertRole.executeUpdate("update truenames set role = '" + scan.nextLine() + "' where truename = '" + heroesList.getString("truename") + "'");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		scan.close();
	}
	
	public void createAdvantageTables() {
		
		try {
            Statement getHeroes = connection.createStatement();
            ResultSet heroesList = getHeroes.executeQuery("select distinct truename from truenames order by truename asc");
            while (heroesList.next()) {
            	long tStart = System.currentTimeMillis();
            	System.out.println(heroesList.getString("truename"));
            	
            	Statement dropTable = connection.createStatement();
            	dropTable.executeUpdate("drop table if exists " + heroesList.getString("truename"));
            	dropTable.close();
            	
            	Statement createTable = connection.createStatement();
            	String table = heroesList.getString("truename");
            	createTable.executeUpdate("create table if not exists " + table + " ( hero text, advantage real )");
                String url = String.format("https://ru.dotabuff.com/heroes/%s/counters", Model.toDotabuffNamingRules(heroesList.getString("truename")));
                DotabuffDataFetcher fetcher = new DotabuffDataFetcher();
                fetcher.setUrl(url);
                HashMap<String, Double> advantageTable = fetcher.fetchAdvantageTable();
                advantageTable.forEach((hero, advantage) -> addAdvantageString(table, createTable, hero, advantage));
                createTable.close();
                System.out.println(System.currentTimeMillis() - tStart);
            }
            heroesList.close();
            getHeroes.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void addAdvantageString(String intoTable, Statement insert, String heroName, double advantage) {
		try {
			insert.executeUpdate("INSERT INTO " + intoTable + " VALUES ('" + heroName + "', " + advantage + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insertTruenamePair(String jargon, String truename) {					// adds a new jargon-truename relation in the DB
		try {
			String sql = "INSERT INTO TRUENAMES (JARGON, TRUENAME) VALUES (?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, jargon);
			stmt.setString(2, truename.replace(' ', '_'));
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
