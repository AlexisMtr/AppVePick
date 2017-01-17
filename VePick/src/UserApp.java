import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;

public class UserApp {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("VePick !");
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("Que voulez vous faire ?");
			System.out.println("1 - Louer un vélo");
			System.out.println("2 - Reserver un vélo");
			System.out.println("3 - Signaler une dégradation au départ");
			System.out.println("4 - Signaler une dégradation à l'arrivé");
			System.out.println("5 - Rendre un vélo");
			System.out.println("6 - Quitter !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						System.out.println("Indiquer le numero de la station :");
						int numStation = sc.nextInt();
						louerVelo(numStation);
						break;
					case 2:
						reserverVelo();
						break;
					case 3:
						signalerDepart();
						break;
					case 4:
						signalerArrivee();
						break;
					case 5:
						rendreVelo();
						break;
					case 6:
						System.exit(0);
						break;
					default:
						break;
				}
			}
			catch(Exception ex)
			{
				System.err.println(ex.getMessage());
			}
			choix = 0;
		}
		
		sc.close();
	}
	

	public static void louerVelo(int station) throws SQLException
	{
		String query = "SELECT vel_id FROM ortizlu.bornette WHERE bor_etat = 'OK' AND vel_id IS NOT NULL AND sta_id = " + station;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = Connexion.connexion().createStatement();
			rs = stmt.executeQuery(query);
			System.out.println("Liste des velos disponibles :");
			while(rs.next())
			{
				int num = rs.getInt(1);
				System.out.println("- " + num);
			}
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
		finally
		{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		}
	}
	public static void reserverVelo()
	{
		
	}
	public static void signalerDepart()
	{
		
	}
	public static void signalerArrivee()
	{
		
	}
	public static void rendreVelo()
	{
		
	}

}
