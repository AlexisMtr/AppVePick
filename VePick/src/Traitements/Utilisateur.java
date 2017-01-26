package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Utilisateur {

	public static int connect(int password) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		int user = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "utilisateur WHERE uti_codeSecret = " + password;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				user = rs.getInt(1);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
		if(user == 0)
			throw new Exception("Aucun utilisateurs trouve !");
		
		return user;
	}
	
	public static int connectConducteur(int id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		int user = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Conducteur WHERE con_id = " + id;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				user = rs.getInt(1);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
		if(user == 0)
			throw new Exception("Aucun conducteur trouve !");
		
		return user;
	}
}
