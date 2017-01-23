package Traitements;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import AccessBD.Connexion;

public class Reservation {
	static Scanner sc = new Scanner(System.in);
	
	public static void reserverVelo(int userId) throws Exception
	{
		int numModeleVelo,numStation;
		String[] dateDebutTab, heureDebutTab, dateFinTab, heureFinTab;
		String query, buffer;
		Statement stmt = null;
		ResultSet rs = null;
		Date dateDebutLocation, dateFinLocation;
		Calendar cal;
		
		//affiche les modèles de vélo
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
		
		//demande le modèle de vélo
		System.out.println("Entrez le numéro du modèle de vélo :");
		numModeleVelo = sc.nextInt();
		sc.nextLine();
		//demande les périodes		
		//debut
		System.out.println("Entrez une date de début de réservation (JJ/MM/AAAA)");
		buffer = sc.nextLine();
		dateDebutTab = buffer.split("/");
		System.out.println("Entrez une heure de début de réservation (00h00)");
		buffer = sc.nextLine();
		heureDebutTab = buffer.split("h");
		cal = new GregorianCalendar(Integer.parseInt(dateDebutTab[2]),Integer.parseInt(dateDebutTab[1])-1,Integer.parseInt(dateDebutTab[0]),
				Integer.parseInt(heureDebutTab[0]),Integer.parseInt(heureDebutTab[1]),00);
		
		dateDebutLocation = new Date(cal.getTimeInMillis());
		
		//fin
		System.out.println("Entrez une date de fin de réservation (JJ/MM/AAAA)");
		buffer = sc.nextLine();
		dateFinTab = buffer.split("/");
		System.out.println("Entrez une heure de fin de réservation (00h00)");
		buffer = sc.nextLine();
		heureFinTab = buffer.split("h");
		cal = new GregorianCalendar(Integer.parseInt(dateFinTab[2]),Integer.parseInt(dateFinTab[1])-1,Integer.parseInt(dateFinTab[0]),
				Integer.parseInt(heureFinTab[0]),Integer.parseInt(heureFinTab[1]),00);
		dateFinLocation = new Date(cal.getTimeInMillis());
		
		//affiche toutes les stations
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
		
		//demande station
		System.out.println("Entrez le numéro de la station voulue :");
		numStation = sc.nextInt();
		
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
	
	public static void annulerReservationVelo(int userId) throws Exception
	{
		int numResa;
		//affiche les résa du user connecté
		Statement stmt = null;
		ResultSet rs = null;
		// query = " ALTER SESSION SET NLS_DATE_FORMAT='DD/MM/YYYY HH24:MI:SS'";
		//stmt = Connexion.connexion().createStatement();
		//stmt.executeQuery(query);
		//if(stmt != null) stmt.close();
		String query = "SELECT res_id, res_deb, res_fin, res_crea, res_statut, mod_libelle, sta_adresse "
				+ "FROM " + Connexion.schemasBD + "reservation "
				+ "NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "NATURAL JOIN " + Connexion.schemasBD + "station "
				+ "WHERE uti_id = "+userId+ " ORDER BY res_id";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Vos réservations :");
			System.out.println("NUM| DATE_DEB |  DATE_FIN  |    DATE DE CREATION    | STATUT | MODELE VELO   |  ADRESSE STATION");
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
			e.printStackTrace();
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
		//demande station
		System.out.println("Entrez le numéro de la réservation choisie :");
		sc.reset();
		numResa = sc.nextInt();
		
		try
		{
			Reservation.UpdateFileAttente(numResa);
			query = "DELETE FROM " + Connexion.schemasBD + "reservation where res_id = " + numResa+"AND uti_id="+userId;
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("La réservation a bien été supprimée !");
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
	
	public static void ValidationReservation(int idReservation, Date dateDebut, Date dateFin, int idStation) throws Exception {
		int countResaChevauche = 0;
		int countBornette = 0;
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT res_deb, res_fin FROM " + Connexion.schemasBD + "Reservation WHERE sta_id ="+idStation+" AND res_deb > SYSDATE AND res_statut ='validee'";
		
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
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
				//update validée
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
	
	//TODO A tester
	public static void UpdateFileAttente(int idReservation) throws Exception {
		Date dateDebut, dateFin;
		int idStation = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT res_deb, res_fin, sta_id FROM " + Connexion.schemasBD + "Reservation WHERE res_id ="+idReservation;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				//attention la date ne contient pas les heures
				dateDebut = rs.getDate("res_deb");
				dateFin = rs.getDate("res_fin");
				idStation = rs.getInt("sta_id");
			}else {
				throw(new Exception("La réservation n'existe pas"));
			}
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			
			if(idStation != 0) {
				query = "SELECT res_deb, res_fin, res_id FROM " + Connexion.schemasBD + "Reservation WHERE res_deb > SYSDATE AND sta_id ="+idStation+"ORDER BY res_crea";
				stmt = Connexion.connexion().createStatement();
				rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					if(dateDebut.before(rs.getDate("res_fin")) && dateFin.after(rs.getDate("res_deb"))) {
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
