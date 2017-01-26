package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Abonnement {

	/**
	 * Permet de creer un nouvel abonne
	 * @param nom - Nom
	 * @param prenom - Prenom
	 * @param CB - Carte bancaire
	 * @param sexe - Sexe (M|F)
	 * @param naissance - Date de naissance
	 * @param adresse - Adresse
	 * @param code - code secret
	 * @return l'identifiant de l'utilisateur
	 * @throws Exception
	 */
	public static int nouvelAbonne(String nom, String prenom, String CB, String sexe, String naissance, String adresse, int code) throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int user = 0;
		
		query = "{CALL " + Connexion.schemasBD + "CreerAbonne("
				+ "'" + CB + "', "
				+ code + ", "
				+ "'" + nom + "', "
				+ "'" + prenom + "', "
				+ "TO_DATE('" + naissance + "', 'DD/MM/YYYY'), "
				+ "'" + sexe + "', "
				+ "'" + adresse + "')}";
		
		System.out.println("QUERY : " + query);
		
		String query2 = "SELECT uti_id FROM " + Connexion.schemasBD + "Utilisateur WHERE uti_codeSecret = " + code;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			
			stmt.close();
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query2);
			if(rs.next())
				user = rs.getInt(1);
			
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

		return user;
	}
	
	/**
	 * Permet de renouveller l'abonnement de l'utilisateur
	 * @param idUser - ID de l'utilisateur abonne
	 * @return <b>true</b> si l'abonnement est renouveller, <b>false</b> sinon
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static boolean renouvellerAbonnement(int idUser) throws Exception
	{
		String query = null;
		Statement stmt = null;
		boolean update = false;
		
		query = "UPDATE " + Connexion.schemasBD + "Abonne SET abo_expirationAbo = SYSDATE WHERE uti_id = " + idUser;
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			update = stmt.executeUpdate(query) == 1 ? true : false;
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
		
		return update;
	}
	
	/**
	 * Creer un nouvel utilisateur non abonne
	 * @param CB - Carte bancaire
	 * @return le code temporaire de l'utilisateur non abonne
	 * @throws Exception - Lève une exception en cas d'erreur et effectue un rollback
	 */
	public static int nouvelUtilisateurNonAbonne(String CB) throws Exception
	{
		//TODO : a tester
		int password = 0;
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int utiId = 0;
		
		query = "{CALL " + Connexion.schemasBD + "CreerNonAbonne('" + CB + "')}";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			
			stmt.close();
			
			// recupere le dernier ID inserer
			query = "SELECT MAX(uti_id) FROM " + Connexion.schemasBD + "Utilisateur";
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				utiId = rs.getInt(1);
			
			stmt.close();
			rs.close();
			
			// selection du code secret lie a cet utilisateur
			query = "SELECT uti_codeSecret FROM " + Connexion.schemasBD + "Utilisateur WHERE uti_id = " + utiId;
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				password = rs.getInt("uti_codeSecret");
			
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
		
		return password;
	}
}
