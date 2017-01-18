import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import AccessBD.Connexion;
import Traitements.Location;
import Traitements.Reservation;
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
			System.out.println("3 - Interface Conducteur");
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
						choixMenuConducteur();
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
			System.out.println("1 - S'abonner (1 an)");
			System.out.println("2 - Renouveller son abonnement (1 an)");
			System.out.println("3 - Louer un vélo");
			System.out.println("4 - Reserver un vélo");
			System.out.println("5 - Annuler une réservation");
			System.out.println("6 - Signaler une dégradation au départ");
			System.out.println("7 - Signaler une dégradation à l'arrivé");
			System.out.println("8 - Rendre un vélo");
			System.out.println("9 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						abonnement();
						break;
					case 2:
						renouvellement(userId);
						break;
					case 3:
						louerVelo();
						break;
					case 4:
						reserverVelo(userId);
						break;
					case 5:
						annulerReservationVelo(userId);
						break;
					case 6:
						signalerDepart();
						break;
					case 7:
						signalerArrivee();
						break;
					case 8:
						rendreVelo();
						break;
					case 9:
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
	
	private static void abonnement() {
		// demander Nom, Prenom, Sexe, Code, ...
	}

	private static void renouvellement(int utilisateurId) {
		// verifier utilisateur et renouveller
	}

	public static void choixMenuSuperviseur()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("[SUPERVISEUR] Que voulez vous faire ?");
			System.out.println("1 - Consulter les Routines");
			System.out.println("2 - Modifier les Routines");
			System.out.println("3 - Consulter les plages V+/V-");
			System.out.println("4 - Modifier les plages V+/V-");
			System.out.println("5 - Voir les velos par stations");
			System.out.println("6 - Voir les velos endommages par station");
			System.out.println("7 - Voir les places disponibles par station");
			System.out.println("8 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						// TODO
						break;
					case 2:
						// TODO
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
						// TODO
						break;
					case 7:
						// TODO
						break;
					case 8:
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
	
	
	public static void choixMenuConducteur()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("[CONDUCTEUR] Que voulez vous faire ?");
			System.out.println("1 - Declarer un velo HS");
			System.out.println("2 - Consulter ma routine");
			System.out.println("3 - Valider/Notifier une tache");
			System.out.println("4 - Deplacer un velo");
			System.out.println("5 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						// TODO
						break;
					case 2:
						// TODO
						break;
					case 3:
						// TODO
						break;
					case 4:
						// TODO
						break;
					case 5:
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
	public static void reserverVelo(int userId)
	{
		try {
			Reservation.reserverVelo(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void annulerReservationVelo(int userId)
	{
		try {
			Reservation.annulerReservationVelo(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
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