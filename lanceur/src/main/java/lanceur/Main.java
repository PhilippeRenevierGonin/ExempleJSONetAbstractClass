package lanceur;

import joueur.Client;
import moteur.Serveur;

public class Main {

    public static void main(String [] args) {
        Client client = new Client("DémoPlayer", 1);
        Serveur serveur = new Serveur();

        serveur.démarrer();
        client.rejoindreUnePartie();

    }
}
