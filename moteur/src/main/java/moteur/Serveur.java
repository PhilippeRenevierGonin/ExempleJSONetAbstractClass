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

    private Object synchroAttenteDebut = new Object();

    public static void main(String [] args) {
        Serveur serveur = new Serveur("127.0.0.1",10101);
        serveur.démarrer();
        serveur.lancerPartie();
    }

    public void lancerPartie() {
        getVue().afficheMessage("on attend que tout le monde soit là");

        synchronized (synchroAttenteDebut) {
            try {
                synchroAttenteDebut.wait();
            } catch (InterruptedException e) {
                getVue().afficheMessageErreur("on a été interrompu, on n'a pas pu attendre que tout le monde soit là");
            }
        }

        getVue().afficheMessage("on peut commencer la partie");
        // lancement de la partie
        // todo...
        // fin de la partie
        fin();

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

        synchronized (synchroAttenteDebut) {
            if (getMoteur().estPartieComplete()) {
                getVue().afficheMessageErreur("on notifie...");
                synchroAttenteDebut.notify();
            }
        }

        return resultat;
    }

    private void fin() {
        getConnexion().envoyerSignalFin(getMoteur().getGagnant());
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
