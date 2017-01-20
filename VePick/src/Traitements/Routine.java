package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import AccessBD.Connexion;

public class Routine {

	public static void visualiserRoutine(int rou_id) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Routine "
				+ "NATURAL JOIN " + Connexion.schemasBD + "Tache "
				+ "NATURAL JOIN " + Connexion.schemasBD + "Conducteur "
				+ "NATURAL JOIN " + Connexion.schemasBD + "Vehicule WHERE rou_id = " + rou_id;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
				int routine = rs.getInt("rou_id");
				Date dateRoutine = rs.getDate("rou_date");
				String immat = rs.getString("veh_immat");
				String modele = rs.getString("veh_modele");
				String conducteur = rs.getString("con_nom") + " " + rs.getString("con_prenom");

				System.out.println("Routine " + routine + " :");
				System.out.println("- Conducteur : " + conducteur);
				System.out.println("- Vehicule : " + modele + " (" + immat + ")");
				System.out.println("- Date : " + dateRoutine);
				System.out.println("- Taches :");

				int num = rs.getInt("tac_ordre");
				String intitule = rs.getString("tac_intitule");
				String comm = rs.getString("tac_commentaire");
				int execute = rs.getInt("tac_execute");
				int sta = rs.getInt("sta_id");
				
				System.out.println("\t- Ordre " + num);
				System.out.println("\t\t- Intitule : " + intitule);
				System.out.println("\t\t- Commentaire : " + comm);
				System.out.println("\t\t- Station : " + sta);
				System.out.println("\t\t- Execute : " + execute);
				while(rs.next())
				{
					num = rs.getInt("tac_ordre");
					intitule = rs.getString("tac_intitule");
					comm = rs.getString("tac_commentaire");
					execute = rs.getInt("tac_execute");
					sta = rs.getInt("sta_id");
					
					System.out.println("\t- Ordre " + num);
					System.out.println("\t\t- Intitule : " + intitule);
					System.out.println("\t\t- Commentaire : " + comm);
					System.out.println("\t\t- Station : " + sta);
					System.out.println("\t\t- Execute : " + execute);
				}
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
	
	public static int routinesConducteur(int conducteurId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT rou_id FROM " + Connexion.schemasBD + "Routine "
				+ "NATURAL JOIN " + Connexion.schemasBD + "Conducteur WHERE con_id = " + conducteurId;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				nb++;
				System.out.println("- Routine " + rs.getInt("rou_id"));
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
	
	public static void validerTache(int rou_id, int tac_ordre) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "UPDATE " + Connexion.schemasBD + "Tache SET tac_execute = 1 WHERE tac_ordre = " + tac_ordre + " AND rou_id = " + rou_id;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
			if(rs != null) rs.close();
		}
		
	}
}
