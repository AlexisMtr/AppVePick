package Traitements;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import AccessBD.Connexion;

public class Station {

	public static void afficherStation() throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Station";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				System.out.println("- Station " + rs.getInt("sta_id") + " : " + rs.getString("sta_adresse"));
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
	}
	
	public static void statsStation(int idStation) throws Exception
	{
		String query = null, nbBornetteQuery = null, nbBornetteLibreQuery = null, nbVeloHSQuery = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "station WHERE sta_id = " + idStation;
		nbBornetteQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation;
		nbBornetteLibreQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette WHERE sta_id = " + idStation + " AND vel_id IS NULL";
		nbVeloHSQuery = "SELECT COUNT(*) FROM " + Connexion.schemasBD + "station "
						+ "NATURAL JOIN " + Connexion.schemasBD + "bornette "
						+ "NATURAL JOIN " + Connexion.schemasBD + "velo WHERE sta_id = " + idStation + " AND vel_etat = 'HS'";
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
				System.out.println("STATION " + rs.getInt("sta_id") + " - " + rs.getString("sta_adresse"));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteQuery);
			while(rs.next())
				System.out.println("- Nb Bornette : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbBornetteLibreQuery);
			while(rs.next())
				System.out.println("- Nb Bornette Libre : " + rs.getInt(1));
			
			stmt.close();
			rs.close();
			
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(nbVeloHSQuery);
			while(rs.next())
				System.out.println("- Nb Velo HS : " + rs.getInt(1));
		
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
	}

	public static int afficherPlages(int idStation) throws Exception {
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Seuil WHERE sta_id=" + idStation;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				nb++;
				System.out.println("- Plage "
						+ rs.getInt("pla_deb") + "H "
						+ "à "
						+ rs.getInt("pla_fin") + "H : "
						+ " seuil V+ =" + rs.getInt("seuilVplus")
						+ " seuil V- =" + rs.getInt("seuilVMoins"));
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
		return nb;
	}

	public static void modifierPlageHoraire(int idStation, int plageDeb, int plageFin, int seuilVmoins, int seuilVplus) throws Exception{
		
		String query = null;
		Statement stmt = null;
		
		query = "UPDATE " + Connexion.schemasBD + "Seuil SET seuilVplus=" + seuilVplus + ", seuilVmoins=" + seuilVmoins + " WHERE sta_id=" + idStation + " AND pla_deb=" + plageDeb + " AND pla_fin=" + plageFin;
		try
		{
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Seuil de la plage horaire modifié !");
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

	public static void ajouterSeuil(int idStation, int plageDeb, int plageFin, int seuilVmoins, int seuilVplus) throws Exception{
		
		String query = null;
		Statement stmt = null;
		
		query = "INSERT INTO " + Connexion.schemasBD + "Seuil VALUES(" + seuilVmoins + ", " + seuilVplus + ", " + idStation + ", " + plageDeb + ", " + plageFin + ")";
		try
		{
			stmt = Connexion.connexion().createStatement();
			stmt.executeQuery(query);
			Connexion.connexion().commit();
			System.out.println("Nouveau seuil ajouté !");
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
	
	public static int afficherBornette(int station)throws Exception
	{
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int nb = 0;
		
		query = "SELECT * FROM " + Connexion.schemasBD + "Bornette WHERE bor_etat = 'OK' AND vel_id IS NULL AND sta_id = " + station;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				nb++;
				System.out.println("- Bornette " + rs.getInt("bor_id"));
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
}
