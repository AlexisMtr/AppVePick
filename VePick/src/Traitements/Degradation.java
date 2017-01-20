package Traitements;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;

public class Degradation {
	static Scanner sc = new Scanner(System.in);
	
	public static void signalerDegradation(int userId, int estDepart) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String commentaire;
		int niveau, locId, numero;
		//demande location
		String query = "SELECT loc_id, loc_deb, loc_fin, mod_libelle "
				+ "FROM " + Connexion.schemasBD + "location "
				+ "NATURAL JOIN " + Connexion.schemasBD + "velo "
				+ "NATURAL JOIN " + Connexion.schemasBD + "modele "
				+ "WHERE uti_id=" + userId + " ORDER BY loc_id";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Vos Locations :");
			System.out.println("Numéro | date début | date fin | vélo :");
			while(rs.next())
			{
				numero = rs.getInt(1); 
				System.out.print(numero + " - ");
				String dateDeb = rs.getString(2);
				System.out.print(dateDeb + " - ");
				String dateFin = rs.getString(3);
				System.out.print(dateFin + " - ");
				String velo = rs.getString(4);
				System.out.println(velo);
			}
			System.out.println();
			System.out.println("Saississez votre numéro de location");
			locId = sc.nextInt();
		}catch(Exception ex){throw ex;}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
		
		//degradation
		System.out.println("Saisissez votre commentaire :");
		sc.nextLine();
		commentaire = sc.nextLine();
		System.out.println("Saississez le niveau de dégradation (1 à 5) :");
		niveau = sc.nextInt();
		
		query = "INSERT INTO " + Connexion.schemasBD + "degradation(deg_id,deg_commentaire,deg_niveau,deg_estdepart,loc_id) "
				+ "VALUES(" + Connexion.schemasBD + "degradation_id.NEXTVAL, "
				+ "'" + commentaire + "', "
				+ niveau + ", "
				+ estDepart + ","
				+ locId + ")";
		try
		{
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
