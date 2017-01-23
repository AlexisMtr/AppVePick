package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Abonnement {

	public static int nouvelAbonne(String nom, String prenom, String CB, String sexe, String naissance, String adresse, int code) throws Exception
	{
		String query = null;
		Statement stmt = null;
		
		query = "{CALL " + Connexion.schemasBD + "CreerAbonne("
				+ "'" + CB + "', "
				+ code + ", "
				+ "'" + nom + "', "
				+ "'" + prenom + "', "
				+ "TO_DATE('" + naissance + "', 'DD/MM/YYYY'), "
				+ "'" + sexe + "', "
				+ "'" + adresse + "')}";
		
		System.out.println("QUERY : " + query);
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			
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
		
		return 0;
	}
	
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
	
	public static int nouvelUtilisateurNonAbonne(String CB) throws Exception
	{
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
			
			query = "SELECT " + Connexion.schemasBD + "utilisateur_id.currval FROM dual";
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				utiId = rs.getInt(1);
			
			stmt.close();
			rs.close();
			
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
