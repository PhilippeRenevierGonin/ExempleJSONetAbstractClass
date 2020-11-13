package moteur.reseau;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.*;
import donnees.Identification;
import donnees.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;

public class EchangesAvecLeClient implements EnvoiDesMessages {
    SocketIOServer serveur;
    RéceptionDesMessages controleur;
    private HashMap<Identification, SocketIOClient> map;

    Object synchro = new Object();

    public EchangesAvecLeClient(String ip, int port, RéceptionDesMessages ctrl) {
        controleur = ctrl;

        map = new HashMap<>();
        Configuration config = new Configuration();
        config.setHostname(ip);
        config.setPort(port);

        // pour éviter un message d'erreur inutile... on attrape l'info sur DisconnectListener
        config.setExceptionListener(new ExceptionListenerAdapter() {
            @Override
            public void onDisconnectException(Exception e, SocketIOClient client) {
                // pour éviter un message d'erreur inutile... on attrape l'info sur DisconnectListener
            }
            @Override
            public boolean exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                return true;
            }
        });


        setServeur(new SocketIOServer(config));

        getServeur().addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                controleur.transfèreMsg("connexion de "+socketIOClient.getRemoteAddress());
            }
        });

        getServeur().addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient socketIOClient) {
                synchronized (synchro) {
                    if (getServeur().getAllClients().size() == 0) {
                        controleur.transfèreMsg("tou.te.s sont déconnecté.e.s");
                        arrêter();
                    }
                }
            }
        });


        // réception d'une identification
        serveur.addEventListener(Message.IDENTIFICATION, Identification.class, new DataListener<Identification>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Identification identification, AckRequest ackRequest) throws Exception {
                synchronized (synchro) {
                    // un seul message ici à la fois
                    if (controleur.nouveauJoueur(identification)) {
                        map.put(identification, socketIOClient);
                    }
                    else {
                        // todo...
                    }
                }
            }
        });


    }


    public void envoyerMessage(Identification leClient, String message, Object... attachement) {
        map.get(leClient).sendEvent(message, attachement);
    }

    public void arrêter() {

        controleur.transfèreMsg("fin du serveur - début");
        for(SocketIOClient c : map.values()) c.disconnect();


        controleur.transfèreMsg("fin du serveur - désabonnement");
        getServeur().removeAllListeners("identification");


        controleur.transfèreMsg("fin du serveur - stop");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getServeur().stop(); // à faire sur un autre thread que sur le thread de SocketIO
                controleur.transfèreMsg("fin du serveur - fin");

            }
        }).start();

    }




    @Override
    public void permettreConnexion() {
        getServeur().start();
    }

    @Override
    public void envoyerSignalFin() {
        // tout le monde a gagné.
        getServeur().getBroadcastOperations().sendEvent(Message.FIN, true);

    }


    /******** pour les propriétés ************/
    public void setServeur(SocketIOServer serveur) {
        this.serveur = serveur;
    }

    public SocketIOServer getServeur() {
        return serveur;
    }
}