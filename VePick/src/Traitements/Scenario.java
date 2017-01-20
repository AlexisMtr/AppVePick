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
		int vel_id = 11;
		
		try
		{
			System.out.println("\n*** Lancement du scénario d'insertion de location*** ");
			
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
			System.out.println("[2] Commit de la location (vel_id=" + vel_id + ") : DONE");	
			
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
