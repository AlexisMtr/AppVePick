package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Station {

	public static void afficherStation() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Station;";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				System.out.println("- Station " + rs.getInt("sta_id") + " : " + rs.getString("sta_adresse"));
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
	}
	
	public static void statsStation(int idStation) throws Exception
	{
		String query = null, nbBornetteQuery = null, nbBornetteLibreQuery = null, nbVeloHSQuery = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "station WHERE sta_id = " + idStation;
		nbBornetteQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation;
		nbBornetteLibreQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation + " AND vel_id IS NULL";
		nbVeloHSQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette "
						+ "NATURAL JOIN " + Connexion.schemasBD + "velo WHERE sta_id = " + idStation + " AND vel_etat = 'HS'";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				System.out.println("STATION " + rs.getInt("sta_id") + " - " + rs.getString("sta_adresse"));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteQuery);
			while(rs.next())
				System.out.println("- Nb Bornette : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteLibreQuery);
			while(rs.next())
				System.out.println("- Nb Bornette Libre : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbVeloHSQuery);
			while(rs.next())
				System.out.println("- Nb Velo HS : " + rs.getInt(1));
		
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
	}
}
