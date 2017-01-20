package Traitements;

import java.sql.CallableStatement;
import java.util.Date;

import AccessBD.Connexion;

public class Abonnement {

	public static int NouvelAbonne(String nom, String prenom, String CB, String sexe, Date naissance, String adresse, int code) throws Exception
	{
		String query = null;
		CallableStatement call = null;
		
		query = "CALL CreerAbonne(?, ?, ?, ?, ?, ?, ?);";
		try
		{
			call = Connexion.connexion().prepareCall(query);
			call.setString(1, CB);
			call.setInt(2, code);
			call.setString(3, nom);
			call.setString(4, prenom);
			call.setDate(5, (java.sql.Date) naissance);
			call.setString(6,  sexe);
			call.setString(7, adresse);
			
			call.executeQuery();
			
			Connexion.connexion().commit();
		}
		catch(Exception ex)
		{
			Connexion.connexion().rollback();
			throw ex;
		}
		finally
		{
			
		}
		
		return 0;
	}
}
