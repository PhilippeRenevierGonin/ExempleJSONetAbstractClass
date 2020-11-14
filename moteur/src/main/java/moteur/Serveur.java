package moteur;

import app.Application;
import app.Vue;
import com.corundumstudio.socketio.SocketIOClient;
import donnees.Identification;
import donnees.action.Action;
import moteur.reseau.EchangesAvecLeClient;
import moteur.reseau.EnvoiDesMessages;
import moteur.reseau.RéceptionDesMessages;
import moteur.vue.VueServeur;

import java.util.HashMap;

public class Serveur extends Application implements RéceptionDesMessages {

    private Moteur moteur;
    private EnvoiDesMessages connexion;

    /**
     * pour attendre la réponse ou la connexion de tou.te.s les joueur.se.s.
     */
    private Object synchroAttenteDebut = new Object();

    /**
     * pour stocker l'action choisie : une seule variable, cela ne marche que pour des demandes séquentielles (on ne fait pas une autre demande tant qu'on n'a pas reçue la réponse
     */
    Action actionChoisie;


    public static void main(String[] args) {
        Serveur serveur = new Serveur("127.0.0.1", 10101);
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
        // pour l'instant on ne fait qu'un tour
        Identification[] joueurs = getMoteur().getJoueurs();
        faireUnTour(joueurs);

        // fin de la partie
        fin();

    }

    /**
     * pour faire un tour de jeu. Il y a une boucle for pour appeler chaque joueur
     *
     * @param joueurs
     */
    protected void faireUnTour(Identification[] joueurs) {
        for (Identification j : joueurs) {
            getConnexion().demandeAuJoueurDeJouer(j, getMoteur().getInventaireDuJoueur(j));
            getVue().afficheMessage("on attend que " + j + " donne son choix");
            synchronized (synchroAttenteDebut) {
                try {
                    synchroAttenteDebut.wait();
                } catch (InterruptedException e) {
                    getVue().afficheMessageErreur("on a été interrompu, on n'a pas pu attendre que " + j + " ait joué");
                }
            }
            // il faut récupérer l'action du joueur, qui a été reçue via socketIO
            // et l'appliquer... on pourrait faire des vérifications (à placer dans les actions)
            Action action = getEtResetActionChoisie();
            if (action.verifier(getMoteur()))  action.appliquerAction(getMoteur());
        }


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
        boolean resultat = getMoteur().ajouterJoueur(id);
        String msg = "Acceptation de ";
        if (!resultat) msg = "Refus de ";
        getVue().afficheMessage(msg + id);

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
     *
     * @param msg le message à afficher
     */
    @Override
    public void transfèreMsg(String msg) {
        transfèreMessage(msg);
    }

    @Override
    public void transfèreAction(Action action) {
        actionChoisie = action;
        synchronized (synchroAttenteDebut) {
            getVue().afficheMessageErreur("on notifie... la réception de la 'action " + action);
            synchroAttenteDebut.notify();
        }
    }

    protected Action getEtResetActionChoisie() {
        Action résultat = actionChoisie;
        actionChoisie = null;
        return résultat;
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
