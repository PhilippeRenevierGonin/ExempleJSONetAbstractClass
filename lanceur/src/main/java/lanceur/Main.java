package lanceur;

import joueur.Client;
import moteur.Serveur;

public class Main {

    public static void main(String [] args) {
        Client client = new Client();
        Serveur serveur = new Serveur();

        serveur.démarrer();
        client.démarrer();

    }
}
