package joueur;

import app.Application;
import com.github.javafaker.Faker;
import donnees.Identification;
import donnees.Inventaire;
import donnees.action.Action;
import joueur.ia.Bot;
import joueur.ia.BotQuiPrendDesRisques;
import joueur.reseau.EchangesAvecLeServeur;
import joueur.vue.VueClient;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Client extends Application {


    private Identification identification;
    private EchangesAvecLeServeur connexion;
    private Bot ia;

    public static void main(String [] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Faker nameFaker = new Faker();

        Bot b;

        if (nameFaker.random().nextInt(10) % 2 == 0) {
            b = new Bot();
        }
        else b = new BotQuiPrendDesRisques();

        Client client = new Client(nameFaker.harryPotter().character(), nameFaker.random().nextInt(99), b);
        client.rejoindreUnePartie();
    }


    public Client(String nom, int lvl, Bot bot) {
        setIdentification(new Identification(nom, lvl));
        setVue(new VueClient(this));
        setIa(bot);
        setConnexion(new EchangesAvecLeServeur("http://127.0.0.1:10101", this));
    }

    public Client(String nom, int lvl) {
        this(nom, lvl, new Bot());
    }


    public void rejoindreUnePartie() {
        getVue().afficheMessage("en attente de déconnexion");
        getConnexion().seConnecter();
    }



    public void transfèreMessage(String msg) {
        getVue().afficheMessage(msg);
    }

    public void signaleErreur(String err) {
        getVue().afficheMessageErreur(err);
    }

    public void aprèsConnexion() {
        getVue().afficheMessage("on est connecté ! et on s'identifie ");
        this.connexion.envoyerId(getIdentification());
    }

    public void finPartie() {
        getVue().finit();
        getConnexion().stop();
    }


    public void résultat(boolean gagné) {
        getVue().afficheMessage(" C'est fini ");

        if (gagné) getVue().afficheMessage("j'ai gagné");
        else getVue().afficheMessage("j'ai perdu");

        finPartie();

    }


    public void jouer(Inventaire inv) {
        getVue().afficheMessage("c'est à moi de jouer "+inv);
        Action actionChoisie = getIa().jouer(inv, getIdentification());
        getVue().afficheMessage("je joue "+actionChoisie);
        getConnexion().envoyerActionChoisie(actionChoisie);
    }



    /********* méthodes pour les propriétés **********/


    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public void setConnexion(EchangesAvecLeServeur connexion) {
        this.connexion = connexion;
    }

    public EchangesAvecLeServeur getConnexion() {
        return connexion;
    }



    public void setIa(Bot ia) {
        this.ia = ia;
    }

    public Bot getIa() {
        return ia;
    }
}
