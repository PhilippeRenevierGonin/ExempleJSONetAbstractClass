package lanceur;

import joueur.Client;
import moteur.Serveur;

public class Main {

    public static void main(String [] args) {
        Client client1 = new Client("DémoPlayer", 1);
        Client client2 = new Client("OtherDémoPlayer", 1);
        Serveur serveur = new Serveur("127.0.0.1",10101);

        // d'abord les clients car lancerPartie est bloquant
        client1.rejoindreUnePartie();
        client2.rejoindreUnePartie();

        serveur.démarrer();
        serveur.lancerPartie();
    }
}
