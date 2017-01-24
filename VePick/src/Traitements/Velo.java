package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import AccessBD.Connexion;

public class Velo {

	/**
	 * Affiche toutes les informations des velo disponible dans la station
	 * @param stationId - ID de la station
	 * @throws Exception - Lève une excpetion en cas d'erreur
	 */
	public static void afficherVelos(int stationId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE vel_id IN (SELECT vel_id FROM " + Connexion.schemasBD + "bornette WHERE sta_id = " + stationId + ")";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
				System.out.println("- Mise en service : " + miseEnService);
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
	
	/**
	 * Affiche tous les velo disponible a la location (OK et associe) de la station
	 * @param stationId - ID de la station
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
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
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
				System.out.println("- Mise en service : " + miseEnService);
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
	
	/**
	 * Associe le velo a la bornette selectionnee
	 * @param veloId - ID du velo a associer
	 * @param borneId - ID de la bornette
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static void associerVelo(int veloId, int borneId) throws Exception
	{
		String query = null;
		Statement stmt = null;

		query = "UPDATE " + Connexion.schemasBD + "bornette SET vel_id = " + veloId + " WHERE bor_id = " + borneId;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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

	/**
	 * Permet de declarer un velo HS
	 * @param vel - ID du velo a declarer HS
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static void declarerHS(int vel) throws Exception
	{
		String query = null;
		Statement stmt = null;

		query = "UPDATE " + Connexion.schemasBD + "Velo SET vel_etat = 'HS' WHERE vel_id = " + vel;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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

	/**
	 * Permet de deplacer un velo (poser le velo dans le vehicule)
	 * @param vel - ID du velo a deplacer
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static void deplacer(int vel) throws Exception
	{
		String query1 = null;
		String query2 = null;
		Statement stmt = null;
	
		query1 = "UPDATE " + Connexion.schemasBD + "Velo SET vel_statut = 'embarque' WHERE vel_id = " + vel;
		query2 = "UPDATE " + Connexion.schemasBD + "Bornette SET vel_id = NULL WHERE vel_id = " + vel;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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

	/**
	 * Permet d'afficher les velos embarque par un vehicule
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static void afficherVelosEmbarque() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE vel_statut = 'embarque'";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
				System.out.println("- Mise en service : " + miseEnService);
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

	/**
	 * Permet de deposer un velo (embarque) dans un centre de reparation
	 * @param centre - ID du centre de reparation
	 * @param vel - ID de=u velo a deposer
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollbak
	 */
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
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
	
	/**
	 * Affiche les centre de reparation
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static void afficherCentresReparation() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "centrereparation";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
