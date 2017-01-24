package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import AccessBD.Connexion;

public class Location {

	/**
	 * Enregistre une nouvelle location de vélo en BD
	 * @param veloId : identifiant du vélo que l'on veut louer
	 * @param userId : identifiant de l'utilisateur
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void LouerVelo(int veloId, int userId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;

		query = "INSERT INTO " + Connexion.schemasBD + "Location(loc_id, loc_deb, uti_id, vel_id) VALUES(" + Connexion.schemasBD + "location_id.nextval, "
				+ "SYSDATE, "
				+ userId + ", "
				+ veloId + ")";
		
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);	
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
	 * Affiche toutes les locations de la BD
	 * @throws Lève une exception en cas d'erreur
	 */
	public static void AfficherLocation() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Location";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				int id = rs.getInt("loc_id");
				Date debut = rs.getDate("loc_deb");	
				Date fin = rs.getDate("loc_fin");
				Double montant = rs.getDouble("loc_montant");
				int resId = rs.getInt("res_id");
				int depart = rs.getInt("loc_departVplus");
				int veloId = rs.getInt("vel_id");
				int userId = rs.getInt("uti_id");
				System.out.println("\nLocation " + id);
				if(debut != null)
					System.out.println("- Debut : " + debut.toLocaleString());
				if(fin != null)
					System.out.println("- Fin : " + fin.toLocaleString());
				else
					System.out.println("- Non terminee");
				if(montant != null)
					System.out.println("- Montant : " + montant);
				System.out.println("- Utilisateur : " + userId);
				System.out.println("- Velo : " + veloId);
				if(resId != 0)
					System.out.println("- Reservation : " + resId);
				else
					System.out.println("- Sans reservation");
				System.out.println("- Depart : " + depart);
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
	 * Associe un vélo à un bornettte
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 * @return Retourne le montant de la location à regler
	 */
	public static Double FinirLocation(int locationId, int borneId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		Double montant = null;
		int veloId = 0;
		
		query = "SELECT vel_id FROM " + Connexion.schemasBD + "Location WHERE loc_id = " + locationId;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				veloId = rs.getInt("vel_id");
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			query = "UPDATE " + Connexion.schemasBD + "Bornette"
					+ " SET vel_id = " + veloId
					+ " WHERE bor_id = " + borneId;
			int OK = stmt.executeUpdate(query);
			stmt.close();
			
			if(OK == 1)
			{
				stmt = Connexion.connexion().createStatement();
				query = "SELECT loc_montant FROM " + Connexion.schemasBD + "Location WHERE loc_id = " + locationId;
				rs = stmt.executeQuery(query);
				montant = rs.getDouble("loc_montant");
				Connexion.connexion().commit();
			}
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
		
		return montant;
	}
	
	/**
	 * Vérifie qu'il existe une location pour un utilisateur enregistré ou non
	 * @param MDP : code secret de l'utilisateur
	 * @param userId : identifiant de l'utilisateur qui à loué
	 * @param veloId : identifiant du vélo loué
	 * @return 0 si il n'y a pas de location; numéro de location si la location existe;
	 * @throws Lève une exception en cas d'erreur
	 */
	public static int VerifierLocation(String MDP, int userId, int veloId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isOk = true;
		int loc_id = 0;

		query = "SELECT uti_id FROM " + Connexion.schemasBD + "Utilisateur WHERE uti_id = " + userId + " AND uti_codeSecret = '" + MDP + "'";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(!rs.next())
				isOk = false;
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			query = "SELECT loc_id FROM " + Connexion.schemasBD + "Location WHERE uti_id = " + userId + " AND vel_id = " + veloId + " AND loc_fin IS NULL";
			rs = stmt.executeQuery(query);
			if(rs.next())
				loc_id = rs.getInt("loc_id");
			else
				isOk = false;
			
			rs.close();
			stmt.close();
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
		return !isOk ? 0 : loc_id;
	}
	
	/**
	 * Affiche les locations d'un utilisateur
	 * @param userId : identifiant de l'utilisateur
	 * @throws Lève une exception en cas d'erreur
	 */
	public static void afficherLocationsDunUtilisateur(int userId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT loc_id, loc_deb, loc_fin, mod_libelle "
				+ "FROM " + Connexion.schemasBD + "location "
				+ "NATURAL JOIN " + Connexion.schemasBD + "velo "
				+ "NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE uti_id=" + userId + " ORDER BY loc_id";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Vos Locations :");
			System.out.println("Numéro | date début | date fin | vélo :");
			while(rs.next())
			{
				int numero = rs.getInt(1); 
				System.out.print(numero + " - ");
				String dateDeb = rs.getString(2);
				System.out.print(dateDeb + " - ");
				String dateFin = rs.getString(3);
				System.out.print(dateFin + " - ");
				String velo = rs.getString(4);
				System.out.println(velo);
			}
			
		}catch(Exception ex){throw ex;}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
}
