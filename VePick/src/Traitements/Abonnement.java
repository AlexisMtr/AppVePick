package Traitements;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import AccessBD.Connexion;

public class Abonnement {

	public static int NouvelAbonne(String nom, String prenom, String CB, String sexe, String naissance, String adresse, int code) throws Exception
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
	
	public static void RenouvellerAbonnement(int idUser) throws Exception
	{
		String query = null;
		Statement stmt = null;
		
		query = "UPDATE " + Connexion.schemasBD + "Abonne SET abo_expirationAbo = SYSDATE WHERE uti_id = " + idUser;
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
		}
	}
	
	public static int NouvelUtilisateurNonAbonne(String CB) throws Exception
	{
		int password = 0;
		String query = null;
		CallableStatement call = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "CALL CreerNonAbonne(?);";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			call = Connexion.connexion().prepareCall(query);
			call.setString(1, CB);
			call.executeQuery();
			
			query = "SELECT uti_code FROM " + Connexion.schemasBD + "NonAbonne WHERE uti_id = " + Connexion.schemasBD + "utilisateur_id.currval;";
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
				password = rs.getInt("uti_code");
			
			Connexion.connexion().commit();
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			if(call != null) call.close();
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
		return password;
	}
}
