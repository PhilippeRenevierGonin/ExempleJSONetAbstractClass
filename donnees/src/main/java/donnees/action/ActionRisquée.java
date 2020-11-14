package donnees.action;

import donnees.Identification;
import donnees.Inventaire;

import java.util.Random;

public class ActionRisquée extends Action{

    /**
     * il faut un constructeur vide
     */
    public ActionRisquée() {

    }

    public ActionRisquée(Identification id) {
        setJoueur(id);
    }


    /**
     * ajoute entre 0 et 3 points
      * @param moteur le moteur qui rappelle cette action pour fournir l'inventaire et les autres informations nécessaires
     */
    @Override
    public void appliquerAction(MoteurDeJeu moteur) {
        if (getJoueur() != null) {
            Random r = moteur.getGenerateurNombreAleatoire();
            Inventaire inventaire = moteur.getInventaireDuJoueur(getJoueur());
            inventaire.ajouterPoints(r.nextInt(4));
        }
    }
}
