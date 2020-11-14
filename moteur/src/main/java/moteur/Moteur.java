package moteur;

import donnees.Identification;
import donnees.Inventaire;
import donnees.action.MoteurDeJeu;
import moteur.reseau.RéceptionDesMessages;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Moteur implements MoteurDeJeu {

    private HashMap<Identification, Inventaire> inventaires = new HashMap<>();
    // ArrayList<Identification> joueurs = new ArrayList<>(); // devenu obsolète, c'est la liste des clefs de la map "inventaures"
    static final int NB_MAX_JOUEUR = 2;
    SecureRandom random = new SecureRandom();

    public Moteur(Serveur serveur) {
    }

    /**
     * s'il y a encore de la place, on ajoute un joueur
     * @param id l'identificaiton du nouveau joueur
     * @return true si l'ajout est effectif, faux si le joueur est rejeté
     */
    public boolean ajouterJoueur(Identification id) {
        Set joueurs = inventaires.keySet();
        if ((joueurs.size() < NB_MAX_JOUEUR) && (! joueurs.contains(id))) {
            inventaires.put(id, new Inventaire());
            return true;
        }
        return false;
    }

    /**
     * pour savoir si la partie est prete à démarrer
     * @return vrai s'il y a NB_MAX_JOUEUR d'enregistrer
     */
    public boolean estPartieComplete() {
        Set joueurs = inventaires.keySet();
        return (joueurs.size() >= NB_MAX_JOUEUR);
    }


    /**
     * condition temporaire : la fin, c'est quand tout le monde est connecté
     * @return vrai si la partie est finie
     */
    public boolean estPartieFinie() {
        Set joueurs = inventaires.keySet();
        return (joueurs.size() >= NB_MAX_JOUEUR);
    }

    /**
     * pour déterminer le gagnant
     * @return le gagnant (le 1er joueur de façon temporaire) si la partie est finie, null sinon
     */
    public Identification getGagnant() {
        if (estPartieFinie()) {
            Set joueurs = inventaires.keySet();
            return (Identification) joueurs.iterator().next();
        }
        else return null;
    }


    /**
     * Pour faire le lien entre une identification et l'iventaire d'un joueur
     * @param joueur le joueur (via son identification) dont on veut l'inventaire
     * @return l'inventaire du joueur concerné ou null si le joueur n'est pas identifié
     */
    @Override
    public Inventaire getInventaireDuJoueur(Identification joueur) {
        return null;
    }

    /**
     * pour des raisons de sécurité et d'efficacité, il en faut qu'un seul générateur de nombre. Il est centralisé dans moteur
     * @return un générateur de nombre aléatoire
     */
    @Override
    public Random getGenerateurNombreAleatoire() {
        return random;
    }

    /**
     * pour itérer sur les joueurs
     * @return un tableau de joueur
     */
    public Identification[] getJoueurs() {
       Set joueurs = inventaires.keySet();
       Identification[] résultat = new Identification[joueurs.size()];
       return  inventaires.keySet().toArray(résultat);
    }
}
