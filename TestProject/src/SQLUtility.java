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
	
	public static void insertTruenamePair(String jargon, String truename) {					// adds a new jargon-truename relation in the DB
		try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(SQLUtility.baseURL, SQLUtility.login, SQLUtility.password);
            try {
            	String sql = "INSERT INTO TRUENAMES (JARGON, TRUENAME) VALUES (?, ?)";
            	PreparedStatement stmt = con.prepareStatement(sql);
            	stmt.setString(1, jargon);
            	stmt.setString(2, truename.replace(' ', '_'));
            	stmt.executeUpdate();
//                stmt.close();
            } finally {
                con.close();
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}