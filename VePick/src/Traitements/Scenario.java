package Traitements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
				System.out.println("Quelle partie du scénario voulez-vous effectuer ? (1|2)");
				choix = sc.nextInt();
			}
			
			System.out.println("\n*** Lancement du scénario d'insertion de location sur vélo HS *** ");
			
			if (choix == 1)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entrée\" pour modifier le vélo...");
				sc.nextLine();
				
				query = "UPDATE " + Connexion.schemasBD + "Velo SET vel_etat = 'HS' "
						+ "WHERE vel_id = " + vel_id;
				
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
				System.out.println("[0] Update du vélo (vel_id=" + vel_id + ") : DONE");
	
				System.out.println("Appuyer sur la touche \"Entrée\" pour commit l'insertion...");
				sc.nextLine();
				
				Connexion.connexion().commit();		
				System.out.println("[1] Commit de l'update (vel_id=" + vel_id + ") : DONE");	
			}
			else if (choix == 2)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entrée\" pour insérer la location...");
				sc.nextLine();
				
				query = "INSERT INTO " + Connexion.schemasBD + "Location(loc_id, loc_deb, uti_id, vel_id) VALUES(" + Connexion.schemasBD + "location_id.nextval, "
						+ "SYSDATE, 4, "
						+ vel_id + ")";
				
				System.out.println("[0] Insert Location (vel_id=" + vel_id + ") : WAIT");
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
	
				System.out.println("Appuyer sur la touche \"Entrée\" pour commit l'insertion...");
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

	public void concurentInsertTacheRoutineDelete(Scanner sc) throws Exception 
	{
		String query = null;
		Statement stmt = null;
		int rou_id = 4;
		
		try
		{
			int choix = 0;
			while(choix != 1 && choix != 2)
			{
				System.out.println("Quelle partie du scénario voulez-vous effectuer ? (1|2)");
				choix = sc.nextInt();
			}
			
			System.out.println("\n*** Lancement du scénario d'insertion de téche dans une routine supprimée ***");
			
			if (choix == 1)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entrée\" pour supprimer toutes les téches d'une routine...");
				sc.nextLine();
				
				query = "DELETE FROM " + Connexion.schemasBD + "tache "
						+ "WHERE rou_id = " + rou_id;
				
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
				System.out.println("[0] Delete téches (rou_id=" + rou_id + ") : DONE");
				
				System.out.println("Appuyer sur la touche \"Entrée\" pour supprimer une routine...");
				sc.nextLine();
				
				query = "DELETE FROM " + Connexion.schemasBD + "routine "
						+ "WHERE rou_id = " + rou_id;
				
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
				System.out.println("[3] Delete téches (rou_id=" + rou_id + ") : DONE");
				

				System.out.println("Appuyer sur la touche \"Entrée\" pour commit la suppression de la routine...");
				sc.nextLine();
				
				Connexion.connexion().commit();		
				System.out.println("[4] Commit du delete (rou_id=" + rou_id + ") : DONE");
				
			}
			else if (choix == 2)
			{
				sc.nextLine();
				System.out.println("Appuyer sur la touche \"Entrée\" pour insérer la tache...");
				sc.nextLine();
				
				query = "INSERT INTO " + Connexion.schemasBD + "tache(rou_id, tac_intitulee) VALUES(" + rou_id + ", 'test')" ;
				
				System.out.println("[0] Insert Tache (vel_id=" + rou_id + ") : WAIT");
				Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				stmt = Connexion.connexion().createStatement();
				stmt.executeQuery(query);
	
				System.out.println("Appuyer sur la touche \"Entrée\" pour commit l'insertion...");
				sc.nextLine();
				
				Connexion.connexion().commit();		
				System.out.println("[1] Commit de l'update (vel_id=" + rou_id + ") : DONE");	
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
