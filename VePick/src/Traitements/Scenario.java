package Traitements;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;

public class Scenario {

	public void concurentInsertLocation(Scanner sc) throws Exception
	{
		String query = null;
		Statement stmt = null;
		int vel_id = 17;
		
		try
		{
			System.out.println("\n*** Lancement du scénario d'insertion de location*** ");
			
			sc.reset();
			System.out.println("Appuyer sur la touche \"Entrée\" pour insérer la location...");
			sc.nextLine();
			
			query = "INSERT INTO " + Connexion.schemasBD + "Location(loc_id, loc_deb, uti_id, vel_id) VALUES(" + Connexion.schemasBD + "location_id.nextval, "
					+ "SYSDATE, 4, "
					+ vel_id + ")";
			
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			System.out.println("[0] Insertion de la location (vel_id=" + vel_id + ") : DONE");
			
			query = "INSERT INTO " + Connexion.schemasBD + "Location(loc_id, loc_deb, uti_id, vel_id) VALUES(" + Connexion.schemasBD + "location_id.nextval, "
					+ "SYSDATE, 5, "
					+ vel_id + ")";

			System.out.println("Appuyer sur la touche \"Entrée\" pour commit l'insertion...");
			sc.nextLine();
			
			Connexion.connexion().commit();		
			System.out.println("[1] Commit de la location (vel_id=" + vel_id + ") : DONE");	
			
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

	public void concurentInsertLocationVeloHS(Scanner sc) throws Exception
	{
		String query = null;
		Statement stmt = null;
		int vel_id = 19;
		
		try
		{
			int choix = 0;
			while(choix != 1 && choix != 2)
			{
				System.out.println("Quelle partie du sc�nario voulez-vous effectuer ? (1|2)");
				choix = sc.nextInt();
			}
			
			System.out.println("\n*** Lancement du sc�nario d'insertion de location sur v�lo HS *** ");
			
			if (choix == 1)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entr�e\" pour modifier le v�lo...");
				sc.nextLine();
				
				query = "UPDATE " + Connexion.schemasBD + "Velo SET vel_etat = 'HS' "
						+ "WHERE vel_id = " + vel_id;
				
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
				System.out.println("[0] Update du v�lo (vel_id=" + vel_id + ") : DONE");
	
				System.out.println("Appuyer sur la touche \"Entr�e\" pour commit l'insertion...");
				sc.nextLine();
				
				Connexion.connexion().commit();		
				System.out.println("[1] Commit de l'update (vel_id=" + vel_id + ") : DONE");	
			}
			else if (choix == 2)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entr�e\" pour ins�rer la location...");
				sc.nextLine();
				
				query = "INSERT INTO " + Connexion.schemasBD + "Location(loc_id, loc_deb, uti_id, vel_id) VALUES(" + Connexion.schemasBD + "location_id.nextval, "
						+ "SYSDATE, 4, "
						+ vel_id + ")";
				
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
				System.out.println("[0] Update du v�lo (vel_id=" + vel_id + ") : DONE");
	
				System.out.println("Appuyer sur la touche \"Entr�e\" pour commit l'insertion...");
				sc.nextLine();
				
				Connexion.connexion().commit();		
				System.out.println("[1] Commit de l'update (vel_id=" + vel_id + ") : DONE");	
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
		}
	}
}
