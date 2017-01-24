package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import AccessBD.Connexion;

public class Velo {

	public static void afficherVelos(int stationId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE vel_id IN (SELECT vel_id FROM " + Connexion.schemasBD + "bornette WHERE sta_id = " + stationId + ")";
		try
		{
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
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	public static void afficherVelosDispo(int stationId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE vel_id IN (SELECT vel_id FROM " + Connexion.schemasBD + "bornette WHERE sta_id = " + stationId + ") "
				+ "AND vel_etat = 'OK' AND vel_statut = 'associe'";
		try
		{
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
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	public static void associerVelo(int veloId, int borneId) throws Exception
	{
		String query = null;
		Statement stmt = null;

		query = "UPDATE " + Connexion.schemasBD + "bornette SET vel_id = " + veloId + " WHERE bor_id = " + borneId;
		try
		{
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query);
			Connexion.connexion().commit();
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
		}
	}

	public static void declarerHS(int vel) throws Exception
	{
		String query = null;
		Statement stmt = null;

		query = "UPDATE " + Connexion.schemasBD + "Velo SET vel_etat = 'HS' WHERE vel_id = " + vel;
		try
		{
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query);
			Connexion.connexion().commit();
			System.out.println("Declaration OK");
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
		}
	}

	public static void deplacer(int vel) throws Exception
	{
		String query1 = null;
		String query2 = null;
		Statement stmt = null;
	
		query1 = "UPDATE " + Connexion.schemasBD + "Velo SET vel_statut = 'embarque' WHERE vel_id = " + vel;
		query2 = "UPDATE " + Connexion.schemasBD + "Bornette SET vel_id = NULL WHERE vel_id = " + vel;
		try
		{
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query1);
			
			stmt.close();
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query2);
			
			Connexion.connexion().commit();
			System.out.println("Le velo est maintenant embarque");
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
		}
	}

	public static void afficherVelosEmbarque() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE vel_statut = 'embarque'";
		try
		{
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
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}

	public static void deposerCentre(int centre, int vel) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "INSERT INTO " + Connexion.schemasBD + "velo_reparation(debutmaintenance, vel_id, cen_id) VALUES(SYSDATE, "
				+ vel + ", "
				+ centre + ")";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Velo depose dans le centre");
			Connexion.connexion().commit();
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	public static void afficherCentresReparation() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "centrereparation";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);

			while(rs.next())
			{
				System.out.println("Centre " + rs.getInt("cen_id"));
				System.out.println("- adresse : " + rs.getString("cen_adresse"));
			}
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
	}
}
