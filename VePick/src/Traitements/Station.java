package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Station {

	/**
	 * Affiche toutes les stations du parc de vélo
	 */
	public static void afficherAllStations() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Station";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
	
	/**
	 * Affiche les statistiques de la station demandée
	 * @param idStation - ID de la station
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static void statsStation(int idStation) throws Exception
	{
		String query = null, nbBornetteQuery = null, nbBornetteLibreQuery = null, nbVeloHSQuery = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		// données de la station (id, adresse ...)
		query = "SELECT * FROM " + Connexion.schemasBD + "station WHERE sta_id = " + idStation;
		// données sur le nombre de bornette totales dans la station
		nbBornetteQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation;
		// données sur le nombre de bornette libre dans la station
		nbBornetteLibreQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation + " AND vel_id IS NULL";
		// données sur le nombre de velo HS dans la station
		nbVeloHSQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette "
						+ "NATURAL JOIN " + Connexion.schemasBD + "velo WHERE sta_id = " + idStation + " AND vel_etat = 'HS'";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				System.out.println("STATION " + rs.getInt("sta_id") + " - " + rs.getString("sta_adresse"));
			
			stmt.close();
			rs.close();
			
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteQuery);
			while(rs.next())
				System.out.println("- Nb Bornette : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteLibreQuery);
			while(rs.next())
				System.out.println("- Nb Bornette Libre : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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

	/**
	 * Affiche les plages horaires d'une station
	 * @param idStation - ID de la station demandée
	 * @return nombre de plages horaires definies pour la station (0 = aucunes plages horaire)
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static int afficherPlages(int idStation) throws Exception {
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Seuil WHERE sta_id=" + idStation;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				nb++;
				System.out.println("- Plage "
						+ rs.getInt("pla_deb") + "H "
						+ "à "
						+ rs.getInt("pla_fin") + "H : "
						+ " seuil V+ =" + rs.getInt("seuilVplus")
						+ " seuil V- =" + rs.getInt("seuilVMoins"));
			}
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
		return nb;
	}

	/**
	 * Permet de modifier la plage horaire d'une station
	 * @param idStation - ID de la station a modifier
	 * @param plageDeb - Heure du début de la plage a modifier
	 * @param plageFin - Heure de la fin de la plage a modifier
	 * @param seuilVmoins - Seuil (en %) au dessous du quel la station passe en V-
	 * @param seuilVplus - Seuil (en %) au dessus du quel la station passe en V+
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static void modifierPlageHoraire(int idStation, int plageDeb, int plageFin, int seuilVmoins, int seuilVplus) throws Exception{
		
		String query = null;
		Statement stmt = null;
		
		query = "UPDATE " + Connexion.schemasBD + "Seuil SET seuilVplus=" + seuilVplus 
				+ ", seuilVmoins=" + seuilVmoins 
				+ " WHERE sta_id=" + idStation 
				+ " AND pla_deb=" + plageDeb 
				+ " AND pla_fin=" + plageFin;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Seuil de la plage horaire modifié !");
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
	 * Permet d'ajouter une plage horaire pour une station
	 * @param idStation - ID de la station
	 * @param plageDeb - Heure du debut de la plage horaire
	 * @param plageFin - Heure de fin de la plage horaire
	 * @param seuilVmoins - Seuil (en %) au dessous du quel la station passe en V-
	 * @param seuilVplus - Seuil (en %) au dessus du quel la station passe en V+
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static void ajouterSeuil(int idStation, int plageDeb, int plageFin, int seuilVmoins, int seuilVplus) throws Exception{
		
		String query = null;
		Statement stmt = null;
		
		query = "INSERT INTO " + Connexion.schemasBD + "Seuil VALUES(" + seuilVmoins + ", " + seuilVplus + ", " + idStation + ", " + plageDeb + ", " + plageFin + ")";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Nouveau seuil ajouté !");
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
	 * Affiche les bornette (bornette OK et libre) de la station passe en parametre
	 * @param station - ID de la station souhaité
	 * @return le nombre de bornette dans la station (libre et OK)
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static int afficherBornette(int station)throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Bornette WHERE bor_etat = 'OK' AND vel_id IS NULL AND sta_id = " + station;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				nb++;
				System.out.println("- Bornette " + rs.getInt("bor_id"));
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

		return nb;
	}
}
