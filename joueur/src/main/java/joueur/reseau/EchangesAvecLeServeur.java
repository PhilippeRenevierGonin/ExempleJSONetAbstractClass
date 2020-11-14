package joueur.reseau;

import donnees.Inventaire;
import donnees.Message;
import donnees.action.Action;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import joueur.Client;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

public class EchangesAvecLeServeur {

    private Client controleur;
    Socket connexion;


    /**
     *
     * @param url : adresse du serveur
     * @param ctrl
     */
    public EchangesAvecLeServeur(String url, Client ctrl) {
        setControleur(ctrl);

        try {
            connexion = IO.socket(url);

            this.controleur.transfèreMessage("on s'abonne à la connection / déconnection ");

            connexion.on("connect", new Emitter.Listener() {
                public void call(Object... objects) {
                    // déplacement du message dans Client/Controleur
                    // on s'identifie
                    controleur.aprèsConnexion();

                }
            });


            connexion.on("disconnect", new Emitter.Listener() {
                public void call(Object... objects) {
                    controleur.transfèreMessage(" !! on est déconnecté !! ");
                    controleur.finPartie();

                }
            });


            this.controleur.transfèreMessage("on s'abonne à fin du jeu ");
            connexion.on(Message.FIN, new Emitter.Listener() {
                public void call(Object... objects) {
                    controleur.résultat((boolean ) objects[0]);
                }
            });

            this.controleur.transfèreMessage("on s'abonne aux demandes de jouer ");
            connexion.on(Message.DEMANDE_DE_JOUER, new Emitter.Listener() {
                public void call(Object... objects) {
                    // objects[0] est un Inventaire
                    JSONObject invJSON = (JSONObject) objects[0];
                    Inventaire inv = new Inventaire();
                    try {
                        inv.setPoints(invJSON.getInt("points"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    controleur.jouer(inv);
                }
            });

        } catch (URISyntaxException e) {
            controleur.signaleErreur(Arrays.toString(e.getStackTrace()));
        }
    }

    public void setControleur(Client controleur) {
        this.controleur = controleur;
    }

    public Client getControleur() {
        return controleur;
    }

    public void stop() {
        connexion.off("connect");
        connexion.off("disconnect");
        connexion.off(Message.FIN);

        // pour ne pas être sur le thread de SocketIO
        new Thread(new Runnable() {
            public void run() {
                connexion.disconnect();
                connexion.close();
                System.out.println("@todo >>>> c'est fini");
                // hack pour arrêter plus vite (sinon attente de plusieurs secondes)
                // System.exit(0);
            }
        }).start() ;
    }

    public void envoyerId(Object pj) {
        JSONObject pieceJointe = new JSONObject(pj);
        connexion.emit(Message.IDENTIFICATION, pieceJointe);
    }

    public void seConnecter() {
        connexion.connect();
    }

    public void envoyerActionChoisie(Object pj) {
        JSONObject pieceJointe = new JSONObject(pj);
        connexion.emit(Message.JOUER_CETTE_ACTION, pieceJointe);
    }
}
