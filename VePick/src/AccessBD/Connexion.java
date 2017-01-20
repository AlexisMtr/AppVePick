package AccessBD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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