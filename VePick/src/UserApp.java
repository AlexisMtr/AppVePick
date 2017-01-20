import java.sql.SQLException;
import java.util.Scanner;

import Traitements.Abonnement;
import Traitements.Degradation;
import Traitements.Location;
import Traitements.Reservation;
import Traitements.Routine;
import Traitements.Scenario;
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
			System.out.println("\nQuelle interface voulez-vous lancer ?");
			System.out.println("1 - Interface Client");
			System.out.println("2 - Interface Superviseur");
			System.out.println("3 - Interface Conducteur");
			System.out.println("4 - Interface Scenario");
			System.out.println("5 - quitter !");
			
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
						choixMenuScenario();
						break;
					case 5:
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
			System.out.println("\n[CLIENT] Que voulez vous faire ?");
			System.out.println("1 - S'abonner (1 an)");
			System.out.println("2 - Renouveller son abonnement (1 an)");
			System.out.println("3 - Louer un v�lo");
			System.out.println("4 - Reserver un v�lo");
			System.out.println("5 - Annuler une r�servation");
			System.out.println("6 - Signaler une d�gradation au d�part");
			System.out.println("7 - Signaler une d�gradation � l'arriv�");
			System.out.println("8 - Rendre un v�lo");
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
						signalerDepart(userId);
						break;
					case 7:
						signalerArrivee(userId);
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
	

	public static void choixMenuSuperviseur()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("\n[SUPERVISEUR] Que voulez vous faire ?");
			System.out.println("1 - Consulter les Routines");
			System.out.println("2 - Supprimer une routines");
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
						consulterRoutines();
						break;
					case 2:
						supprimerRoutine();
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
	
	private static void supprimerRoutine() throws Exception {
		int rou_id_choix;
		Routine.afficherAllRoutines();
		
		//demande routine id
		System.out.println("Numéro choisi");
		rou_id_choix = sc.nextInt();
		
		//supprimer tâches de routine
		Routine.supprimerTacheDeRoutine(rou_id_choix);
	}


	private static void consulterRoutines() throws Exception {
		int rou_id_choix;
		Routine.afficherAllRoutines();
		
		//demande routine id
		System.out.println("Numéro choisi");
		rou_id_choix = sc.nextInt();
		
		//visualiserRoutine
		Routine.visualiserRoutine(rou_id_choix);
		
	}


	public static void choixMenuConducteur()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("\n[CONDUCTEUR] Que voulez vous faire ?");
			System.out.println("1 - Declarer un velo HS");
			System.out.println("2 - Consulter mes routines");
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
						consulterRoutine(userId);
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
	

	public static void choixMenuScenario()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("\n[SCENARIO] Que voulez vous faire ?");
			System.out.println("1 - Location en même temps");
			System.out.println("2 - TODO");
			System.out.println("3 - TODO");
			System.out.println("4 - TODO");
			System.out.println("5 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						new Scenario().concurentInsertLocation(sc);
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
	


	private static void consulterRoutine(int conducteur) {
		try
		{
			int nbRoutine = Routine.routinesConducteur(conducteur);
			if(nbRoutine == 0)
				System.out.println("Vous n'avez pas de routine");
			else
			{
				System.out.println("Quelle routine consulter ?");
				int routine = sc.nextInt();
				Routine.visualiserRoutine(routine);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void abonnement() {	
		try
		{
			//sc.reset();
			System.out.println("Votrz nom : ");
			String nom = "Hugo";
			System.out.println(nom);
			System.out.println("Votre prenom : ");
			String prenom = "Luc";
			System.out.println(prenom);
			System.out.println("Votre date de naissance (JJ/MM/AAAA) : ");
			String naissance = "08/04/1995";
			System.out.println(naissance);
			System.out.println("Votre sexe (M|F) : ");
			String sexe = "M";
			System.out.println(sexe);
			System.out.println("Votre adresse : ");
			String adresse = "Rue de la Chimie";
			System.out.println(adresse);
			System.out.println("Votre code : ");
			int code = 159951;
			System.out.println(code);
			System.out.println("Votre CB : ");
			String CB = "1596547893215084";
			System.out.println(CB);
			
			Abonnement.nouvelAbonne(nom, prenom, CB, sexe, naissance, adresse, code);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void renouvellement(int utilisateurId) {
		try
		{
			if(Abonnement.renouvellerAbonnement(utilisateurId))
				System.out.println("Abonnement renouveller pour 1 an");
			else
				System.err.println("Impossible de renouveller l'abonnement");
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void afficherVelos() {
		try
		{
			System.out.println("Quelle station ?");
			int sta = sc.nextInt();
			
			Velo.afficherVelos(sta);
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

			Velo.afficherVelosDispo(sta);
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
	
	public static void signalerDepart(int userId)
	{
		try {
			Degradation.signalerDegradation(userId,1);
		} catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	public static void signalerArrivee(int userId)
	{
		try {
			Degradation.signalerDegradation(userId,0);
		} catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	public static void rendreVelo()
	{
		try
		{
			System.out.println("Quel velo associez-vous ?");
			int veloId = sc.nextInt();
			System.out.println("Sur quelle bornette ?");
			int borneId = sc.nextInt();
			
			Velo.associerVelo(veloId, borneId);
			
			System.out.println("Saisir votre MDP");
			sc.nextLine();
			String password = sc.nextLine();
			
			int locationId = Location.VerifierLocation(password, userId, veloId);
			if(locationId != 0)
				System.out.println("Montant paye : " + Location.FinirLocation(locationId, borneId) + "�");
			else
				System.err.println("Aucune locations trouvees");
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

}