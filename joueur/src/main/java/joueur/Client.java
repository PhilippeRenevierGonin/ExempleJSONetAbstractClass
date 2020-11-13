package joueur;

import app.Application;
import donnees.Identification;
import joueur.reseau.EchangesAvecLeServeur;
import joueur.vue.VueClient;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Client extends Application {


    private Identification identification;
    private EchangesAvecLeServeur connexion;

    public static void main(String [] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Client client = new Client("Michel", 99);
        client.rejoindreUnePartie();
    }


    public Client(String nom, int lvl) {
        setIdentification(new Identification(nom, lvl));
        setVue(new VueClient(this));
        setConnexion(new EchangesAvecLeServeur("http://127.0.0.1:10101", this));
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


    public void résultat(boolean gagné) {
        getVue().afficheMessage(" C'est fini ");

        if (gagné) getVue().afficheMessage("j'ai gagné");
        else getVue().afficheMessage("j'ai perdu");

        finPartie();

    }
}
