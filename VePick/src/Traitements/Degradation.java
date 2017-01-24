package Traitements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;

public class Degradation {
	static Scanner sc = new Scanner(System.in);
	
	/**
	 * Enregistre une nouvelle déclaration dans la BD
	 * @param userId : identifiant de l'utilisateur concerné
	 * @param estDepart : 1 si le signalement de la dégradation se fait au départ; 0 sinon
	 * @param commentaire : commentaire de la dégradation
	 * @param niveau : niveau de dégradation de vélo
	 * @param locId : identifiant de la location concerné par la dégradation
	 * @throws En cas d'erreur : provoque un rollback et lève une exception
	 */
	public static void signalerDegradation(int userId, int estDepart, String commentaire, int niveau, int locId) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query;

		query = "INSERT INTO " + Connexion.schemasBD + "degradation(deg_id,deg_commentaire,deg_niveau,deg_estdepart,loc_id) "
				+ "VALUES(" + Connexion.schemasBD + "degradation_id.NEXTVAL, "
				+ "'" + commentaire + "', "
				+ niveau + ", "
				+ estDepart + ","
				+ locId + ")";
		try
		{
			Connexion.connexion().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("La dégradation à bien été enregistrée !");
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
