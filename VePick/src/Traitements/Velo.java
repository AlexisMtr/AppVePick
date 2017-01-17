package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import AccessBD.Connexion;

public class Velo {
	public static String schema = "maalexis.";

	public static void AfficherVelos(int stationId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			query = "SELECT * FROM " + schema + "velo NATURAL JOIN " + schema + " modele WHERE vel_id IN (SELECT vel_id FROM " + schema + "bornette WHERE sta_id = " + stationId + ")";
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				Date miseEnService = rs.getDate("vel_miseEnService");
				int veloId = rs.getInt("vel_id");
				String modele = rs.getString("mod_libelle");
				Double montant = rs.getDouble("mod_tarif");
				String etat = rs.getString("vel_etat");
				String statut = rs.getString("vel_statut");
				
				System.out.println("Velo " + veloId);
				System.out.println("- Mise en service : " + miseEnService.toLocaleString());
				System.out.println("- Modele : " + modele);
				System.out.println("- Tarif /h : " + montant);
				System.out.println("- Etat : " + etat);
				System.out.println("- Statut : " + statut);
			}
		}
		catch(Exception ex)
		{
			System.err.println("ERROR : " + ex.getMessage());
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	public static void AfficherVelosDispo(int stationId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			query = "SELECT * FROM " + schema + "velo NATURAL JOIN " + schema + " modele WHERE vel_id IN (SELECT vel_id FROM " + schema + "bornette WHERE sta_id = " + stationId + ") AND vel_etat = 'OK' AND vel_statut = 'associe'";
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				Date miseEnService = rs.getDate("vel_miseEnService");
				int veloId = rs.getInt("vel_id");
				String modele = rs.getString("mod_libelle");
				Double montant = rs.getDouble("mod_tarif");
				String etat = rs.getString("vel_etat");
				String statut = rs.getString("vel_statut");
				
				System.out.println("Velo " + veloId);
				System.out.println("- Mise en service : " + miseEnService.toLocaleString());
				System.out.println("- Modele : " + modele);
				System.out.println("- Tarif /h : " + montant);
				System.out.println("- Etat : " + etat);
				System.out.println("- Statut : " + statut);
			}
		}
		catch(Exception ex)
		{
			System.err.println("ERROR : " + ex.getMessage());
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	public static void AssocierVelo(int veloId, int borneId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		//ResultSet rs = null;
		
		try
		{
			query = "UPDATE " + schema + "bornette SET vel_id = " + veloId + " WHERE bor_id = " + borneId;
			
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query);
		}
		catch(Exception ex)
		{
			System.err.println("ERROR : " + ex.getMessage());
		}
		finally
		{
			if(stmt != null) stmt.close();
		}
	}
}
