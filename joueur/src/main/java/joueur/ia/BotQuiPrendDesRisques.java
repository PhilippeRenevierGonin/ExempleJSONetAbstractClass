package joueur.ia;

import donnees.Identification;
import donnees.Inventaire;
import donnees.action.Action;
import donnees.action.ActionRisquée;

public class BotQuiPrendDesRisques extends Bot {
    public Action jouer(Inventaire inv, Identification moi) {
        return new ActionRisquée(moi);
    }

}
