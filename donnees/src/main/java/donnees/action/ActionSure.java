package donnees.action;

import donnees.Identification;
import donnees.Inventaire;

public class ActionSure extends Action {

    /**
     * il faut un constructeur vide
     */
    public ActionSure() {
    }

    public ActionSure(Identification id) {
        setJoueur(id);
    }


    @Override
    public void appliquerAction(MoteurDeJeu moteur) {
        if (getJoueur() != null) {
            Inventaire inventaire = moteur.getInventaireDuJoueur(getJoueur());
            inventaire.ajouterPoints(2);
        }
    }

    public String toString() {
        return "ActionSure jouée par "+getJoueur();
    }
}
