import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;
import Traitements.Location;
import Traitements.Velo;

public class UserApp {

	public static int userId;
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		userId = 2;
		System.out.println("VePick !");
		
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("Quelle interface voulez-vous lancer ?");
			System.out.println("1 - Interface Client");
			System.out.println("2 - Interface Superviseur");
			System.out.println("3 - Interface ...");
			System.out.println("4 - quitter !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						choixMenuClient();
						break;
					case 2:
						choixMenuSuperviseur();
						break;
					case 3:
						// TODO
						break;
					case 4:
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
	
	public static void choixMenuClient()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("[CLIENT] Que voulez vous faire ?");
			System.out.println("1 - Louer un vélo");
			System.out.println("2 - Reserver un vélo");
			System.out.println("3 - Signaler une dégradation au départ");
			System.out.println("4 - Signaler une dégradation à l'arrivé");
			System.out.println("5 - Rendre un vélo");
			System.out.println("6 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						louerVelo();
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
						sortir = true;
						break;
					default:
						break;
				}
			}
			catch(Exception ex)
			{
				System.err.println(ex.getMessage());
			}
			
			if (sortir)
				break;
			
			choix = 0;
		}
	}
	
	public static void choixMenuSuperviseur()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("[SUPERVISEUR] Que voulez vous faire ?");
			System.out.println("1 - Voir les locations");
			System.out.println("2 - Afficher velos");
			System.out.println("3 - TODO");
			System.out.println("4 - TODO");
			System.out.println("5 - TODO");
			System.out.println("6 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						voirLocations();
						break;
					case 2:
						afficherVelos();
						break;
					case 3:
						// TODO
						break;
					case 4:
						// TODO
						break;
					case 5:
						// TODO
						break;
					case 6:
						sortir = true;
						break;
					default:
						break;
				}
			}
			catch(Exception ex)
			{
				System.err.println(ex.getMessage());
			}
			
			if (sortir)
				break;
			
			choix = 0;
		}
	}
	
	

	private static void afficherVelos() {
		try
		{
			System.out.println("Quelle station ?");
			int sta = sc.nextInt();
			
			Velo.AfficherVelos(sta);
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

	private static void voirLocations() {
		try
		{
			Location.AfficherLocation();
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

	public static void louerVelo() throws SQLException
	{
		try
		{
			System.out.println("Quelle station ?");
			int sta = sc.nextInt();

			Velo.AfficherVelosDispo(sta);
			System.out.println("Quel velo ?");
			
			int velo = sc.nextInt();
			Location.LouerVelo(velo, userId);
			
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
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
		try
		{
			System.out.println("Quel velo associez-vous ?");
			int veloId = sc.nextInt();
			System.out.println("Sur quelle bornette ?");
			int borneId = sc.nextInt();
			
			Velo.AssocierVelo(veloId, borneId);
			
			System.out.println("Saisir votre MDP");
			sc.nextLine();
			String password = sc.nextLine();
			
			int locationId = Location.VerifierLocation(password, userId, veloId);
			if(locationId != 0)
				System.out.println("Montant paye : " + Location.FinirLocation(locationId, borneId) + "€");
			else
				System.err.println("Aucune locations trouvees");
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

}
