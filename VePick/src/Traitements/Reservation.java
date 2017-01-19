package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;

public class Reservation {
	static Scanner sc = new Scanner(System.in);
	
	public static void reserverVelo(int userId) throws Exception
	{
		int numModeleVelo;
		String dateDebutLocation, dateFinLocation;
		int numStation;
		//affiche les mod�les de v�lo
		String query = "SELECT DISTINCT mod_id, mod_libelle FROM " + Connexion.schemasBD + "velo NATURAL JOIN " + Connexion.schemasBD + "modele ORDER BY mod_id";
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Liste des mod�les de velos disponibles :");
			while(rs.next())
			{
				int idMod = rs.getInt(1);
				System.out.print(idMod + " - ");
				String libelleMod = rs.getString(2);
				System.out.println(libelleMod);
			}
			System.out.println();
			stmt.close();
			rs.close();
		}catch(Exception ex){throw ex;}
		
		//demande le mod�le de v�lo
		System.out.println("Entrez le num�ro du mod�le de v�lo :");
		numModeleVelo = sc.nextInt();
		
		//demande les p�riodes (d�but et fin)
		System.out.println("Entrez une date de d�but location (JJ/MM/YYYY)");
		sc.nextLine();
		dateDebutLocation = sc.nextLine();
		System.out.println("Entrez une date de fin location (JJ/MM/YYYY)");
		dateFinLocation = sc.nextLine();
		
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
			stmt.close();
			rs.close();
		}catch(Exception ex){throw ex;}
		
		//demande station
		System.out.println("Entrez le num�ro de la station voulue :");
		numStation = sc.nextInt();
		
		query = "INSERT INTO " + Connexion.schemasBD + "reservation(res_id,res_deb,res_fin,mod_id,sta_id,uti_id) "
				+ "VALUES(" + Connexion.schemasBD + "reservation_id.NEXTVAL, "
				+ "TO_DATE('" + dateDebutLocation + "', 'dd/mm/yyyy'),"
				+ "TO_DATE('" + dateFinLocation + "', 'dd/mm/yyyy'),"
				+ numModeleVelo + ","
				+ numStation + ","
				+ userId + ")";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Votre r�servation � bien �t� enregistr�e !");
			stmt.close();
			rs.close();
		}catch(Exception ex){throw ex;}
		
	}
	
	public static void annulerReservationVelo(int userId) throws Exception
	{
		int numResa;
		//affiche les r�sa du user connect�
		String query = "SELECT res_id, res_deb, res_fin, res_crea, res_statut, mod_libelle, sta_adresse "
				+ "FROM " + Connexion.schemasBD + "reservation "
				+ "NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "NATURAL JOIN " + Connexion.schemasBD + "station "
				+ "WHERE uti_id = "+userId+ " ORDER BY res_id";
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Vos r�servations :");
			System.out.println("NUM| DATE_DEB |  DATE_FIN  |    DATE DE CREATION    | STATUT | MODELE VELO   |  ADRESSE STATION");
			System.out.println("----------------------------------------------------------------------------------------------");
			while(rs.next())
			{
				int res_id = rs.getInt(1);
				System.out.print(res_id + " - ");
				String res_deb = rs.getString(2);
				System.out.print(res_deb.substring(0, 10) + " - ");
				String res_fin = rs.getString(3);
				System.out.print(res_fin.substring(0, 10) + " - ");
				String res_crea = rs.getString(4);
				System.out.print(res_crea + " - ");
				String res_statut = rs.getString(5);
				System.out.print(res_statut + " - ");
				String mod_libelle = rs.getString(6);
				System.out.print(mod_libelle + " - ");
				String sta_adresse = rs.getString(7);
				System.out.println(sta_adresse);
			}
			System.out.println();
			stmt.close();
			rs.close();
		}catch(Exception ex){throw ex;}
		
		//demande station
		System.out.println("Entrez le num�ro de la r�servation choisie :");
		sc.reset();
		numResa = sc.nextInt();
		
		query = "DELETE FROM " + Connexion.schemasBD + "reservation where res_id = " + numResa;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("La r�servation � bien �t� supprim�e !");
			stmt.close();
			rs.close();
		}catch(Exception ex){throw ex;}
	}
}
