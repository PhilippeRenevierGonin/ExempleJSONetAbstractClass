package moteur;

import donnees.Identification;
import moteur.reseau.RéceptionDesMessages;

import java.util.ArrayList;

public class Moteur  {

    ArrayList<Identification> joueurs = new ArrayList<>();
    static final int NB_MAX_JOUEUR = 2;

    public Moteur(Serveur serveur) {
    }

    /**
     * s'il y a encore de la place, on ajoute un joueur
     * @param id l'identificaiton du nouveau joueur
     * @return true si l'ajout est effectif, faux si le joueur est rejeté
     */
    public boolean ajouterJoueur(Identification id) {
        if ((joueurs.size() < NB_MAX_JOUEUR) && (! joueurs.contains(id))) {
            joueurs.add(id);
            return true;
        }
        return false;
    }

    /**
     * pour savoir si la partie est prete à démarrer
     * @return vrai s'il y a NB_MAX_JOUEUR d'enregistrer
     */
    public boolean estPartieComplete() {
        return (joueurs.size() >= NB_MAX_JOUEUR);
    }


    /**
     * condition temporaire : la fin, c'est quand tout le monde est connecté
     * @return vrai si la partie est finie
     */
    public boolean estPartieFinie() {
        return (joueurs.size() >= NB_MAX_JOUEUR);
    }

    /**
     * pour déterminer le gagnant
     * @return le gagnant (le 1er joueur de façon temporaire) si la partie est finie, null sinon
     */
    public Identification getGagnant() {
        if (estPartieFinie()) return joueurs.get(0);
        else return null;
    }
}
