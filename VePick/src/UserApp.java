import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import Traitements.Abonnement;
import Traitements.Degradation;
import Traitements.Location;
import Traitements.Reservation;
import Traitements.Routine;
import Traitements.Scenario;
import Traitements.Station;
import Traitements.Utilisateur;
import Traitements.Velo;

public class UserApp {

	public static int userId;
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		userId = 1;
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
		int mdp;
		while(choix == 0)
		{
			System.out.println("\n[CLIENT] Que voulez vous faire ?");
			System.out.println("1 - S'abonner (1 an)");
			System.out.println("2 - Renouveller son abonnement (1 an)");
			System.out.println("3 - Louer un vélo");
			System.out.println("4 - Reserver un vélo");
			System.out.println("5 - Annuler une réservation");
			System.out.println("6 - Signaler une dégradation au départ");
			System.out.println("7 - Signaler une dégradation à l'arrivé");
			System.out.println("8 - Rendre un vélo");
			System.out.println("9 - Non Inscrit (pour test)");
			System.out.println("10 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						abonnement();
						break;
					case 2:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						renouvellement(userId);
						break;
					case 3:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						louerVelo(userId);
						break;
					case 4:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						reserverVelo(userId);
						break;
					case 5:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						annulerReservationVelo(userId);
						break;
					case 6:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						signalerDepart(userId);
						break;
					case 7:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						signalerArrivee(userId);
						break;
					case 8:
						System.out.println("Votre MDP ?");
						mdp = Integer.parseInt(sc.next());
						userId = Utilisateur.connect(mdp);
						rendreVelo();
						break;
					case 9:
						utilisateurNonAbonne();
						break;
					case 10:
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
	

	private static void utilisateurNonAbonne() throws Exception {
		System.out.println("Votre CB : ");
		String CB = sc.next();
		//System.out.println(CB);
		System.out.println("Votre MDP : " + Abonnement.nouvelUtilisateurNonAbonne(CB));
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
			System.out.println("3 - Ajouter une tâche à une routine");
			System.out.println("4 - Consulter les plages V+/V-");
			System.out.println("5 - Modifier les plages V+/V-");
			System.out.println("6 - Voir les stats par stations");
			System.out.println("7 - sortir !");
			
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
						ajouterTache();
						break;
					case 4:
						consulterPlages();
						break;
					case 5:
						modifierPlageVplusVmoins();
						break;
					case 6:
						afficherStatStation();
						break;
					case 7:
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


	private static void modifierPlageVplusVmoins() throws Exception {
		Station.afficherAllStations();
		System.out.println("Quelle station voulez-vous ?");
		int idStation = sc.nextInt();
		if(Station.afficherPlages(idStation) == 0)
		{
			System.out.println("Pas de seuils pour cette station");
			System.out.println("Voulez-vous ajouter des seuils ? (O/N)");
			sc.nextLine();
			char answer = sc.nextLine().charAt(0);
			if (answer == 'O')
			{
				//affiche les plages horaire
				Station.afficherPlages(idStation);
				//demande quelle plage il veut
				System.out.println("Quelle plage voulez vous modifier ?");
				System.out.println("heure debut ?");
				int plageDeb = sc.nextInt();
				System.out.println("heure fin ?");
				int plageFin = sc.nextInt();
				//demande quels seuil il veut
				System.out.println("seuil V+ ?");
				int seuilVmoins = sc.nextInt();
				System.out.println("seuil V- ?");
				int seuilVplus = sc.nextInt();
				//insert le seuil
				Station.ajouterSeuil(idStation, plageDeb, plageFin, seuilVmoins, seuilVplus);
			}
		}else
		{
			System.out.println("Entrez les infos de la plage horaire à modifier : ");
			System.out.println("plage Debut ?");
			int plageDeb = sc.nextInt();
			System.out.println("plage Fin ?");
			int plageFin = sc.nextInt();
			System.out.println("nouveau seuil V+ ?");
			int seuilVmoins = sc.nextInt();
			System.out.println("nouveau seuil V- ?");
			int seuilVplus = sc.nextInt();
			Station.modifierPlageHoraire(idStation, plageDeb, plageFin, seuilVmoins, seuilVplus);
		}
	}


	private static void consulterPlages() throws Exception {
		Station.afficherAllStations();
		System.out.println("Quelle station voulez-vous ?");
		int idStation = sc.nextInt();
		Station.afficherPlages(idStation);
	}


	private static void afficherStatStation() throws Exception {
		Station.afficherAllStations();
		System.out.println("Quelle station voulez-vous ?");
		int idStation = sc.nextInt();
		Station.statsStation(idStation);
	}


	private static void supprimerRoutine() throws Exception {
		int rou_id;
		Routine.afficherAllRoutines();
		
		//demande routine id
		System.out.println("Numéro choisi");
		rou_id = sc.nextInt();
		
		//supprimer tâches de routine
		Routine.supprimerRoutine(rou_id);
	}
	
	private static void ajouterTache() throws Exception {
		int rou_id, sta_id;
		String libelle;
		//demander station id
		System.out.println("Liste des stations : ");
		Station.afficherAllStations();
		System.out.println("Numéro de la station choisie");
		sta_id = sc.nextInt();
		
		//demande routine id
		Routine.afficherAllRoutines();
		System.out.println("Numéro de routine choisie");
		rou_id = sc.nextInt();
		
		//demande du libellé
		System.out.println("Libellé pour la tâche : ");
		sc.nextLine();
		libelle = sc.nextLine();
		//ajouter tâche à une routine
		Routine.ajouterTache(rou_id, libelle, sta_id);
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
		int id;
		while(choix == 0)
		{
			System.out.println("\n[CONDUCTEUR] Que voulez vous faire ?");
			System.out.println("1 - Declarer un velo HS");
			System.out.println("2 - Consulter mes routines");
			System.out.println("3 - Valider/Notifier une tache");
			System.out.println("4 - Deplacer un velo");
			System.out.println("5 - Deposer en centre");
			System.out.println("6 - Deposer un velo");
			System.out.println("7 - sortir !");
			
			choix  = sc.nextInt();
			try
			{
				switch(choix)
				{
					case 1:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						declarerVeloHS();
						break;
					case 2:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						consulterRoutine(userId);
						break;
					case 3:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						validerTache(userId);
						break;
					case 4:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						deplacerVelo();
						break;
					case 5:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						deposerEnCentre();
						break;
					case 6:
						System.out.println("Votre ID ?");
						id = Integer.parseInt(sc.next());
						userId = Utilisateur.connectConducteur(id);
						deposerVelo();
						break;
					case 7:
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
	

	private static void deposerVelo() throws Exception
	{
		Station.afficherAllStations();
		System.out.println("Quelle station ?");
		int sta = sc.nextInt();
		int nb = Station.afficherBornette(sta);
		if(nb != 0)
		{
			System.out.println("Quelle bornette ?");
			int borneId = sc.nextInt();
			System.out.println("Quel velo ? (0 pour annuler)");
			int veloId = sc.nextInt();
			
			if(veloId != 0)
				Velo.associerVelo(veloId, borneId);
		}
		else
			System.out.println("Pas de bornette disponible dans cette station !");
	}


	private static void deposerEnCentre() throws Exception
	{
		Velo.afficherCentresReparation();
		System.out.println("Quel centre ?");
		int centre = sc.nextInt();
		Velo.afficherVelosEmbarque();
		System.out.println("Quel velo ? (0 pour annuler)");
		int vel = sc.nextInt();	
		if(vel != 0)
			Velo.deposerCentre(centre, vel);
	}


	private static void deplacerVelo() throws Exception
	{
		Station.afficherAllStations();
		System.out.println("Quelle station ?");
		int sta = sc.nextInt();
		Velo.afficherVelos(sta);
		System.out.println("Quel velo ? (0 pour annuler)");
		int vel = sc.nextInt();
		if(vel != 0)
			Velo.deplacer(vel);
	}


	private static void declarerVeloHS() throws Exception {
		Station.afficherAllStations();
		System.out.println("Quelle station ?");
		int sta = sc.nextInt();
		Velo.afficherVelos(sta);
		System.out.println("Quel velo ? (0 pour annuler)");
		int vel = sc.nextInt();
		if(vel != 0)
			Velo.declarerHS(vel);		
	}


	private static void validerTache(int userId) throws Exception
	{
		System.out.println("Valider ou Notifier ? [V|N]");
		String choix = sc.next();
		
		Routine.routinesConducteur(userId);
		System.out.println("Quelle routine ?");
		int routine_id = sc.nextInt();
		Routine.visualiserRoutine(routine_id);
		System.out.println("Quelle tache ? (0 pour annuler)");
		int tache_id = sc.nextInt();
		
		if(tache_id == 0) return;

		System.out.println("Commentaire a destination de votre superviseur (1 ligne) : ");
		String comm = sc.nextLine();
		
		if(choix.equals("V"))
			Routine.validerTache(routine_id, tache_id, comm);
		else
			Routine.notifierTache(routine_id, tache_id, comm);
	}


	public static void choixMenuScenario()
	{
		boolean sortir = false;
		int choix = 0;
		while(choix == 0)
		{
			System.out.println("\n[SCENARIO] Que voulez vous faire ?");
			System.out.println("1 - Location en méme temps");
			System.out.println("2 - Location vélo HS");
			System.out.println("3 - Création d'une tâche dans une routine supprimée");
			System.out.println("4 - Modification statut bornette");
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
						new Scenario().concurentInsertLocationVeloHS(sc);
						break;
					case 3:
						new Scenario().concurentInsertTacheRoutineDelete(sc);
						break;
					case 4:
						new Scenario().concurentUpdateBornette(sc);
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
			sc.reset();
			System.out.println("Votre nom : ");
			String nom = sc.next();
			//System.out.println(nom);
			System.out.println("Votre prenom : ");
			String prenom = sc.next();
			//System.out.println(prenom);
			System.out.println("Votre date de naissance (JJ/MM/AAAA) : ");
			String naissance = sc.next();
			//System.out.println(naissance);
			System.out.println("Votre sexe (M|F) : ");
			String sexe = sc.next();
			//System.out.println(sexe);
			System.out.println("Votre adresse : ");
			String adresse = sc.next();
			//System.out.println(adresse);
			System.out.println("Votre code : ");
			int code = Integer.parseInt(sc.next());
			//System.out.println(code);
			System.out.println("Votre CB : ");
			String CB = sc.next();
			//System.out.println(CB);
			
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
	
	public static void afficherVelos() {
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

	public static void voirLocations() {
		try
		{
			Location.AfficherLocation();
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

	public static void louerVelo(int user) throws Exception
	{
		int sta, velo;
		System.out.println("Quelle station ?");
		sta = sc.nextInt();

		Velo.afficherVelosDispo(sta);
		System.out.println("Quel velo ?");
		
		velo = sc.nextInt();
		Location.LouerVelo(velo, user);
	}
	
	public static void reserverVelo(int userId)
	{
		try {
			int numModeleVelo;
			String[] dateDebutTab, dateFinTab, heureDebutTab, heureFinTab;
			String buffer;
			Date dateDebutLocation, dateFinLocation;
			Calendar cal;
			int numStation;
			
			Reservation.afficherAllModelesVelo();
			System.out.println("Entrez le numéro du modèle de vélo :");
			numModeleVelo = sc.nextInt();

			//demande les périodes de début et fin
			System.out.println("Entrez une date de début de réservation (JJ/MM/AAAA)");
			buffer = sc.nextLine();
			buffer = sc.nextLine();
			dateDebutTab = buffer.split("/");
			System.out.println("Entrez une heure de début de réservation (00h00)");
			buffer = sc.nextLine();
			heureDebutTab = buffer.split("h");
			cal = new GregorianCalendar(Integer.parseInt(dateDebutTab[2]),Integer.parseInt(dateDebutTab[1])-1,Integer.parseInt(dateDebutTab[0]),
					Integer.parseInt(heureDebutTab[0]),Integer.parseInt(heureDebutTab[1]),00);
			dateDebutLocation = new Date(cal.getTimeInMillis());
			
			System.out.println("Entrez une date de fin de réservation (JJ/MM/AAAA)");
			buffer = sc.nextLine();
			dateFinTab = buffer.split("/");
			System.out.println("Entrez une heure de fin de réservation (00h00)");
			buffer = sc.nextLine();
			heureFinTab = buffer.split("h");
			cal = new GregorianCalendar(Integer.parseInt(dateFinTab[2]),Integer.parseInt(dateFinTab[1])-1,Integer.parseInt(dateFinTab[0]),
					Integer.parseInt(heureFinTab[0]),Integer.parseInt(heureFinTab[1]),00);
			dateFinLocation = new Date(cal.getTimeInMillis());
			
			System.out.println("Entrez le numéro de la station voulue :");
			numStation = sc.nextInt();
			
			Reservation.reserverVelo(userId, numStation, numModeleVelo, dateDebutTab, dateFinTab, heureDebutTab, heureFinTab, dateDebutLocation, dateFinLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void annulerReservationVelo(int userId) throws Exception
	{
		int numResa;
		
		Reservation.afficherReservationsDuUserConnecte(userId);
		System.out.println("Entrez le numéro de la réservation choisie :");
		sc.reset();
		numResa = sc.nextInt();
		Reservation.annulerReservationVelo(userId,numResa);
		Reservation.UpdateFileAttente(numResa);
	}
	
	public static void signalerDepart(int userId) throws Exception
	{
		String commentaire;
		int niveau, locId;
		
		Location.afficherLocationsDunUtilisateur(userId);
		System.out.println();
		System.out.println("Saississez votre numéro de location");
		locId = sc.nextInt();
		System.out.println("Saisissez votre commentaire :");
		sc.nextLine();
		commentaire = sc.nextLine();
		System.out.println("Saississez le niveau de dégradation (1 à 5) :");
		niveau = sc.nextInt();
		Degradation.signalerDegradation(userId,1, commentaire, niveau, locId);
	}
	
	public static void signalerArrivee(int userId) throws Exception
	{
		String commentaire;
		int niveau, locId;
		
		Location.afficherLocationsDunUtilisateur(userId);
		System.out.println();
		System.out.println("Saississez votre numéro de location");
		locId = sc.nextInt();
		System.out.println("Saisissez votre commentaire :");
		sc.nextLine();
		commentaire = sc.nextLine();
		System.out.println("Saississez le niveau de dégradation (1 à 5) :");
		niveau = sc.nextInt();
		Degradation.signalerDegradation(userId,0, commentaire, niveau, locId);
	}
	
	public static void rendreVelo()
	{
		try
		{
			String codeRemise;
			System.out.println("Quel velo associez-vous ?");
			int veloId = sc.nextInt();
			System.out.println("Sur quelle bornette ?");
			int borneId = sc.nextInt();
			
			System.out.println("Saisir votre MDP");
			sc.nextLine();
			String password = sc.nextLine();
			
			int locationId = Location.VerifierLocation(password, userId, veloId);
			System.out.println("Beneficiez vous d'une remise ? (non abonne uniquement) (O/N)");
			String remise = sc.nextLine();
			if(remise.equals("O"))
			{
				System.out.println("Merci de saisir le code : ");
				codeRemise = sc.nextLine();
			}
			else
				codeRemise = "";
			
			if(locationId != 0)
				System.out.println("Montant paye : " + Location.FinirLocation(locationId, borneId, codeRemise) + "€");
			else
				System.err.println("Aucune locations trouvees");
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}

}