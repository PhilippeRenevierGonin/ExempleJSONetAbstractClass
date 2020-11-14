package joueur.ia;

import donnees.Identification;
import donnees.Inventaire;
import donnees.action.Action;
import donnees.action.ActionSure;

public class Bot {
    public Action jouer(Inventaire inv, Identification moi) {
        return new ActionSure(moi);
    }
}
