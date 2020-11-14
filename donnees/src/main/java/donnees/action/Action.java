package donnees.action;

import donnees.Identification;

public abstract class Action {
    /**
     * le joueur qui fait le choix de cette action
      */
    Identification joueur;

    public Identification getJoueur() {
        return joueur;
    }

    public void setJoueur(Identification joueur) {
        this.joueur = joueur;
    }

    /**
     * application de l'action choisie, implémentée dans les classes concrètes
     * @param moteur le moteur qui rappelle cette action pour fournir l'inventaire et les autres informations nécessaires
     */
    public abstract void appliquerAction(MoteurDeJeu moteur) ;
}
