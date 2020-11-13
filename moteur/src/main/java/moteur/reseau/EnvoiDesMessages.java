package moteur.reseau;

import donnees.Identification;

public interface EnvoiDesMessages {


    void permettreConnexion();

    void envoyerSignalFin(Identification gagnant);
}
