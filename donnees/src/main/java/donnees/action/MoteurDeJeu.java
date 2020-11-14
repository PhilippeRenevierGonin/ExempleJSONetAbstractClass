package donnees.action;

import donnees.Identification;
import donnees.Inventaire;

public interface MoteurDeJeu {

    public Inventaire getInventaireDuJoueur(Identification joueur);
}
