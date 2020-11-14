package moteur.reseau;

import donnees.Identification;
import donnees.Inventaire;

public interface EnvoiDesMessages {


    void permettreConnexion();

    void envoyerSignalFin(Identification gagnant);

    void demandeAuJoueurDeJouer(Identification j, Inventaire inventaireDuJoueur);
}
