package moteur.reseau;

import donnees.Identification;

public interface RéceptionDesMessages {

    boolean nouveauJoueur(Identification id) ;

    void transfèreMsg(String msg);

}
