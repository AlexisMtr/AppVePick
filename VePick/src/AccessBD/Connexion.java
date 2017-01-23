package AccessBD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connexion {

	public final static String configurationFile = "src/AccessBD/DB.properties";
	/** 
	 * A modifier si besoin
	 * <br>
	 * <b>/!\ Le point à la fin du schema est déjà présent /!\</b> 
	 **/
	public final static String schemasBD = "ortizlu.";
	private static Connection connexion = null;
	
	public static Connection connexion() throws IOException, ClassNotFoundException, SQLException
	{
		if (connexion != null)
			return connexion;

		try
		{
			DBAccesProperties dbAccess = new DBAccesProperties(configurationFile);
			connexion = DriverManager.getConnection(dbAccess.getDBUrl(), dbAccess.getUsername(), dbAccess.getPassword());
			connexion.setAutoCommit(false);
			// TODO DATE complète
			String query = " ALTER SESSION SET NLS_DATE_FORMAT='DD/MM/YYYY HH24:MI:SS'";
			Statement stmt = connexion.createStatement();
			stmt.executeQuery(query);
			if(stmt != null) stmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connexion;
	}
	
	public static Connection connexionTemporaire(String username, String password) throws IOException, ClassNotFoundException, SQLException
	{
		Connection tempCo = null;
		try
		{
			DBAccesProperties dbAccess = new DBAccesProperties(configurationFile);
			tempCo = DriverManager.getConnection(dbAccess.getDBUrl(), username, password);
			tempCo.setAutoCommit(false);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tempCo;
	}
}