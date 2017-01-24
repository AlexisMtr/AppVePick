package Traitements;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import AccessBD.Connexion;

public class Reservation {
	
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Enregistre une nouvelle Réservation et lui attribut un statut
	 * @param userId : identifiant de l'utilisateur
	 * @param numStation : identifiant de la station
	 * @param numModeleVelo : identifiant du vélo concerné
	 * @param dateDebutTab : date de début séparée et réparte dans un tableau
	 * @param dateFinTab : date de fin séparée et réparte dans un tableau
	 * @param heureDebutTab : heure de début de la réservation
	 * @param heureFinTab : heure de fin de la réservation
	 * @param dateDebutLocation : date de début de la réservation
	 * @param dateFinLocation : date de fin de la réservation
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void reserverVelo(int userId, int numStation,int numModeleVelo, String[] dateDebutTab, String[] dateFinTab, String[] heureDebutTab, String[] heureFinTab, Date dateDebutLocation, Date dateFinLocation) throws Exception
	{	//TODO vérifier recouvrement
		
		String query;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "INSERT INTO " + Connexion.schemasBD + "reservation(res_id,res_deb,res_fin,mod_id,sta_id,uti_id) "
				+ "VALUES(" + Connexion.schemasBD + "reservation_id.NEXTVAL, "
				+ "TO_DATE('"+dateDebutTab[0]+"/"+dateDebutTab[1]+"/"+dateDebutTab[2]+" "+heureDebutTab[0]+":"+heureDebutTab[1]+":00','DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_DATE('"+dateFinTab[0]+"/"+dateFinTab[1]+"/"+dateFinTab[2]+" "+heureFinTab[0]+":"+heureFinTab[1]+":00', 'DD/MM/YYYY HH24:MI:SS'),"
				+ numModeleVelo + ","
				+ numStation + ","
				+ userId + ")";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Votre réservation a bien été enregistrée !");
			
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			
			query ="SELECT " + Connexion.schemasBD + "reservation_id.CURRVAL FROM dual";
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				ValidationReservation(rs.getInt(1),dateDebutLocation,dateFinLocation,numStation);
			}
		}
		catch(Exception e)
		{
			Connexion.connexion().rollback();
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
	}
	
	
	
	/**
	 * Affiche tous les modèles de vélo existant dans la BD
	 * @throws Lève une exception en cas d'erreur
	 */
	public static void afficherAllModelesVelo() throws Exception
	{
		String query;
		Statement stmt = null;
		ResultSet rs = null;

		query = "SELECT DISTINCT mod_id, mod_libelle FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele ORDER BY mod_id";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Liste des modèles de velos disponibles :");
			while(rs.next())
			{
				int idMod = rs.getInt(1);
				System.out.print(idMod + " - ");
				String libelleMod = rs.getString(2);
				System.out.println(libelleMod);
			}
			System.out.println();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	/**
	 * Affiche toutes les stations de la BD
	 * @throws Lève une exception en cas d'erreur
	 */
	public static void afficherAllStations() throws Exception
	{
		String query;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT sta_id, sta_adresse FROM " + Connexion.schemasBD + "station";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Liste des stations :");
			while(rs.next())
			{
				int numSta = rs.getInt(1);
				System.out.print(numSta + " - ");
				String adresseSta = rs.getString(2);
				System.out.println(adresseSta);
			}
			System.out.println();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	/**
	 * Supprime une réservation donnée pour un utilisateur donné
	 * @param userId : identifiant de l'utisateur
	 * @param numResa : identifiant de la réservation
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void annulerReservationVelo(int userId, int numResa) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query;
		
		try
		{
			query = "DELETE FROM " + Connexion.schemasBD + "reservation where res_id = " + numResa+"AND uti_id="+userId;
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("La réservation a bien été supprimée !");
		}
		catch(Exception e)
		{
			Connexion.connexion().rollback();
			throw e;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	/**
	 * Affiche les réservations de l'utilisateur connecté
	 * @throws Lève une exception en cas d'erreur
	 */
	public static void afficherReservationsDuUserConnecte(int userId) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query;

		query = "SELECT res_id, res_deb, res_fin, res_crea, res_statut, mod_libelle, sta_adresse "
				+ "FROM " + Connexion.schemasBD + "reservation "
				+ "NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "NATURAL JOIN " + Connexion.schemasBD + "station "
				+ "WHERE uti_id = "+userId+ " ORDER BY res_id";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Vos réservations :");
			System.out.println("NUM |    DATE_DEB    |     DATE_FIN     |  DATE DE CREATION  | STATUT |  MODELE VELO  |  ADRESSE STATION");
			System.out.println("----------------------------------------------------------------------------------------------");
			while(rs.next())
			{
				int res_id = rs.getInt(1);
				System.out.print(res_id + " - ");
				String res_deb = rs.getString(2);
				System.out.print(res_deb.substring(0, 16) + " - ");
				String res_fin = rs.getString(3);
				System.out.print(res_fin.substring(0, 16) + " - ");
				String res_crea = rs.getString(4);
				System.out.print(res_crea.substring(0, 16) + " - ");
				String res_statut = rs.getString(5);
				System.out.print(res_statut + " - ");
				String mod_libelle = rs.getString(6);
				System.out.print(mod_libelle + " - ");
				String sta_adresse = rs.getString(7);
				System.out.println(sta_adresse);
			}
			System.out.println();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	/**
	 * Compte le nombre de réservations max possibles dans une station donnée pour une période donnée (nombre de bornette/2)
	 * Si le nombre de réservations pour cette période donnée alors UPDATE la réservation et la met "valide"
	 * Sinon la réservation est mise sur la liste d'attente
	 * @param idReservation : identifiant de la réservation à vérifier
	 * @param dateDebut : date de début de la réservation
	 * @param dateFin : date de fin de la réservation
	 * @param idStation : identifiant de la station où l'on veut réserver un vélo
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void ValidationReservation(int idReservation, Date dateDebut, Date dateFin, int idStation) throws Exception {
		int countResaChevauche = 0;
		int countBornette = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query;

		query = "SELECT res_deb, res_fin FROM " + Connexion.schemasBD + "Reservation WHERE sta_id ="+idStation+" AND res_deb > SYSDATE AND res_statut ='validee'";
		
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				//Attention heure non prise en compte
				if(dateDebut.before(rs.getDate("res_fin")) && dateFin.after(rs.getDate("res_deb"))) {
					countResaChevauche ++;
				}
			}
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			
			query = "SELECT count(*) FROM " + Connexion.schemasBD + "Bornette WHERE sta_id ="+idStation;
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				countBornette = rs.getInt(1);
			}
			
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			
			if(countBornette != 0 && countResaChevauche < countBornette/2) {
				//update validé
				query = "UPDATE " + Connexion.schemasBD + "Reservation SET res_statut = 'validee' WHERE res_id ="+idReservation;
				System.out.println("La réservation a été validée");
				stmt = Connexion.connexion().createStatement();
				rs = stmt.executeQuery(query);
				Connexion.connexion().commit();
			}
			else {
				System.out.println("La réservation a été mise sur liste d'attente");
			}
		}
		catch(Exception e)
		{
			Connexion.connexion().rollback();
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	
	/**
	 * Met à jour la file d'attente si la réservation annulée était validée
	 * Valide la réservation qui à été ajoutée en premier sur la liste d'attente (sur la même période)
	 * @param idReservation : identifiant de la réservation
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void UpdateFileAttente(int idReservation) throws Exception {
		java.util.Date dateDebut=null, dateFin=null;
		int idStation = 0;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String query;
		
		query = "SELECT res_deb, res_fin, sta_id, res_statut FROM " + Connexion.schemasBD + "Reservation WHERE res_id ="+idReservation;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				if(rs.getString("res_statut").equals("validee"))
				{
					//formattage de la chaine de caractère car rs.getDate ne retourne pas l'heure
					dateDebut = formatter.parse(rs.getString("res_deb").substring(0, 19));
					dateFin = formatter.parse(rs.getString("res_fin").substring(0, 19));
					idStation = rs.getInt("sta_id");
				}				
			}else {
				throw(new Exception("La réservation n'existe pas"));
			}
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			
			if(idStation != 0) {
				query = "SELECT res_deb, res_fin, res_id FROM " + Connexion.schemasBD + "Reservation WHERE res_deb > SYSDATE AND res_statut = 'en_attente' AND sta_id ="+idStation+"ORDER BY res_crea";
				stmt = Connexion.connexion().createStatement();
				rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					if(dateDebut.before(formatter.parse(rs.getString("res_fin").substring(0, 19))) && dateFin.after(formatter.parse(rs.getString("res_deb").substring(0, 19)))) {
						query = "UPDATE " + Connexion.schemasBD + "Reservation SET res_statut = 'validee' WHERE res_id ="+rs.getInt("res_id");
						if(stmt != null) stmt.close();
						if(rs != null) rs.close();
						stmt = Connexion.connexion().createStatement();
						rs = stmt.executeQuery(query);
						Connexion.connexion().commit();
						break;
					}
				}
			}
		}
		catch(Exception e)
		{
			Connexion.connexion().rollback();
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
}
