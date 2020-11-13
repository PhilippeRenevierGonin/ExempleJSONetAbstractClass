package lanceur;

import joueur.Client;
import moteur.Serveur;

public class Main {

    public static void main(String [] args) {
        Client client = new Client("DémoPlayer", 1);
        Serveur serveur = new Serveur("127.0.0.1",10101);

        serveur.démarrer();
        client.rejoindreUnePartie();

    }
}
