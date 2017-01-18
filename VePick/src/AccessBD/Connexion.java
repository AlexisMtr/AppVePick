package AccessBD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	public final static String configurationFile = "src/AccessBD/DB.properties";
	public final static String schemasBD = "ortizlu";
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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connexion;
	}
}