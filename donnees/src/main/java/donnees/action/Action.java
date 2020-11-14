package donnees.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import donnees.Identification;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionRisquée.class, name = "ActionRisquée"),
        @JsonSubTypes.Type(value = ActionSure.class, name = "ActionSure")
})
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

    /**
     * pour vérifier si l'action est légale
     * méthode à surcharger dans chaque classe concrète, avec une implémentation par défaut
     * Par défaut : retourne toujours true
     * @param moteur : objet qui gère la partie en cours
     * @return true si l'action est légale, false sinon
     */
    public boolean verifier(MoteurDeJeu moteur) {
        return true;
    }
}
