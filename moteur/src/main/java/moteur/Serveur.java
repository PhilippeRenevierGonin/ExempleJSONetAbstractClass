package moteur;

import app.Application;
import app.Vue;
import com.corundumstudio.socketio.SocketIOClient;
import commun.Identification;
import moteur.reseau.EchangesAvecLeClient;
import moteur.reseau.EnvoiDesMessages;
import moteur.reseau.RéceptionDesMessages;
import moteur.vue.VueServeur;

public class Serveur extends Application implements RéceptionDesMessages {

    private Moteur moteur;
    private EnvoiDesMessages connexion;

    public static void main(String [] args) {
        Serveur serveur = new Serveur("127.0.0.1",10101);
        serveur.démarrer();
    }

    public Serveur(String ip, int port) {
        setVue(new VueServeur());
        setMoteur(new Moteur(this));
        setConnexion(new EchangesAvecLeClient(ip, port, this));
    }

    public void démarrer() {
        getConnexion().permettreConnexion();
    }

    @Override
    public boolean nouveauJoueur(donnees.Identification id) {
        boolean resultat =  getMoteur().ajouterJoueur(id);
        String msg = "Acceptation de ";
        if (! resultat) msg = "Refus de ";
        getVue().afficheMessage(msg+id);

        // fin de la partie
        fin();

        return resultat;
    }

    private void fin() {
        getConnexion().envoyerSignalFin();
    }

    /**
     * adaptation de la liaison vue - application avec la liaison connexion - serveur
     * @param msg le message à afficher
     */
    @Override
    public void transfèreMsg(String msg) {
        transfèreMessage(msg);
    }


    public void setMoteur(Moteur moteur) {
        this.moteur = moteur;
    }

    public Moteur getMoteur() {
        return moteur;
    }

    public void setConnexion(EnvoiDesMessages connexion) {
        this.connexion = connexion;
    }

    public EnvoiDesMessages getConnexion() {
        return connexion;
    }


}
