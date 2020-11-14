package moteur.reseau;

import donnees.Identification;
import donnees.action.Action;

public interface RéceptionDesMessages {

    boolean nouveauJoueur(Identification id) ;

    void transfèreMsg(String msg);

    void transfèreAction(Action action);

}
