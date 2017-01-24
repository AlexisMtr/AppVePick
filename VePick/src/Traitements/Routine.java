package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

import AccessBD.Connexion;

public class Routine {
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Affiche les informations de la routine passe en parametre
	 * @param rou_id - ID de la routine
	 * @throws Exception - Lève une excpetion en cas d'erreur
	 */
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
			// passe la transaction en mode READ COMMITTED
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);

			// affiche les informations de la routine (Conducteur, Date, Vehicule, liste des taches)
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
	
	/**
	 * Affiche les differentes routine liés a un condcuteur
	 * @param conducteurId - ID du conducteur
	 * @return le nombre de routine associes au condcuteur
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static int routinesConducteur(int conducteurId) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT rou_id FROM " + Connexion.schemasBD + "Routine WHERE con_id = " + conducteurId;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				// compte le onombre de routines
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
	
	/**
	 * Permet de valider une tache
	 * @param rou_id - ID de la routine concernée
	 * @param tac_ordre - Ordre d'execution de la tache
	 * @param commentaire - Commentaire associé à l'execution de la tache
	 * @throws Exception - Lève une excpetion en cas d'erreur et effectue un rollback
	 */
	public static void validerTache(int rou_id, int tac_ordre, String commentaire) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "UPDATE " + Connexion.schemasBD + "Tache SET tac_execute = 1, tac_commentaire = '" + commentaire + "' WHERE tac_ordre = " + tac_ordre + " AND rou_id = " + rou_id;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query);
			Connexion.connexion().commit();
			System.out.println("Tache validee");
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
	 * Permet de notifier une tache
	 * @param rou_id - ID de la routine concernee
	 * @param tac_ordre - Ordre d'execution de la tache
	 * @param commentaire - Commentaire associe a la notification
	 * @throws Exception - Lève une exeception en cas d'erreur et effectue un rollbak
	 */
	public static void notifierTache(int rou_id, int tac_ordre, String commentaire) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "INSERT INTO " + Connexion.schemasBD + "Notification VALUES(" 
				+ Connexion.schemasBD + "notification_id.nextval, SYSDATE, "
				+ "'" + commentaire + "',"
				+ tac_ordre + ", "
				+ rou_id + ")";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeUpdate(query);
			Connexion.connexion().commit();
			System.out.println("Tache notifiee");
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
	 * Affiche toutes les routines
	 * @throws Exception - Lève une exception en cas d'erreur
	 */
	public static void afficherAllRoutines() throws Exception
	{
		// affiche toutes les routines
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int idRoutine;
		String date, nomConducteur, Immat;
		
		query = "SELECT rou_id, rou_date, con_nom, veh_immat FROM " + Connexion.schemasBD + "Routine NATURAL JOIN " + Connexion.schemasBD + "Conducteur ORDER BY rou_id";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Toutes les routine :");
			System.out.println("Numéro | Date | Conducteur | Immatriculation");
			while(rs.next())
			{
				idRoutine = rs.getInt(1);
				System.out.print(idRoutine + " - ");
				date = rs.getString(2);
				System.out.print(date + " - ");
				nomConducteur = rs.getString(3);
				System.out.print(nomConducteur + " - ");
				Immat = rs.getString(4);
				System.out.println(Immat);
			}
			System.out.println();
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
	 * Supprime les taches d'une routine et la routine
	 * @param rou_id - ID de la routine concerné
	 * @throws Exception - Lève une excpetion en cas d'erreur et effectue un rollbak
	 */
	public static void supprimerRoutine(int rou_id) throws Exception {
		Statement stmt= null;
		
		String query1 = "DELETE FROM " + Connexion.schemasBD + "tache WHERE rou_id=" + rou_id;
		String query2 = "DELETE FROM " + Connexion.schemasBD + "routine WHERE rou_id=" + rou_id;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			//supprimer les tâches de la routine
			stmt.executeQuery(query1);
			stmt.executeQuery(query2);
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
		System.out.println("La routine à bien été supprimée !");
	}
	
}
