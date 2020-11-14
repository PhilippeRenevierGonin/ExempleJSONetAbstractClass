package donnees.action;

import donnees.Identification;
import donnees.Inventaire;

import java.util.Random;

public interface MoteurDeJeu {

    public Inventaire getInventaireDuJoueur(Identification joueur);

    public Random getGenerateurNombreAleatoire();
}
